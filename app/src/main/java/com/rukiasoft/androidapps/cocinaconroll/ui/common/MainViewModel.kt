package com.rukiasoft.androidapps.cocinaconroll.ui.common

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import androidx.palette.graphics.Palette
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.rukiasoft.androidapps.cocinaconroll.R
import com.rukiasoft.androidapps.cocinaconroll.extensions.switchMap
import com.rukiasoft.androidapps.cocinaconroll.firebase.FirebaseConstants
import com.rukiasoft.androidapps.cocinaconroll.firebase.models.RecipeFirebase
import com.rukiasoft.androidapps.cocinaconroll.firebase.models.TimestampFirebase
import com.rukiasoft.androidapps.cocinaconroll.persistence.PersistenceManager
import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Ingredient
import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Recipe
import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Step
import com.rukiasoft.androidapps.cocinaconroll.persistence.relations.RecipeWithInfo
import com.rukiasoft.androidapps.cocinaconroll.persistence.utils.PersistenceConstants
import com.rukiasoft.androidapps.cocinaconroll.persistence.utils.QueryMaker
import com.rukiasoft.androidapps.cocinaconroll.preferences.PreferencesConstants
import com.rukiasoft.androidapps.cocinaconroll.preferences.PreferencesManager
import com.rukiasoft.androidapps.cocinaconroll.resources.ResourcesManager
import com.rukiasoft.androidapps.cocinaconroll.utils.ReadWriteUtils
import com.rukiasoft.androidapps.cocinaconroll.utils.ViewUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.util.*
import javax.inject.Inject


/**
 * Copyright (C) Rukiasoft - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Roll <raulfeliz@gmail.com>, January 2019
 *
 *
 */

class MainViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val persistenceManager: PersistenceManager,
    private val queryMaker: QueryMaker,
    private val readWriteUtils: ReadWriteUtils,
    private val viewUtils: ViewUtils,
    private val resourcesManager: ResourcesManager
) : ViewModel() {

    private val allowedRecipesCheck = 1
    private val forbiddenRecipesCheck = 2
    private val personalRecipesCheck = 4

    private var downloaded = false
    private val downloading: MutableLiveData<Int> = MutableLiveData()
    private var downloadingValue = 0
    private val nextRecipeToDownloadImage: LiveData<List<Recipe>>
    private var recipesBeingDownloaded: MutableList<String> = mutableListOf()
    private var recipesBeingUploaded: MutableList<String> = mutableListOf()
    private var picturesBeingUploaded: MutableList<String> = mutableListOf()
    private var recipesBeingDeleted: MutableList<String> = mutableListOf()

    private val query: MutableLiveData<Pair<FilterType, String?>> = MutableLiveData()
    private var listOfRecipes: LiveData<PagedList<Recipe>>
    private val numberOfOwnRecipes: LiveData<Int> = persistenceManager.numberOfOwnRecipes()
    private val listOfRecipesToUpdateInServer: LiveData<List<RecipeWithInfo>>
    private val listOfPicturesToUpdateInServer: LiveData<List<Recipe>>
    private val listOfPicturesToDeleteInServer: LiveData<List<Recipe>>

    enum class FilterType {
        ALL,
        STARTER,
        MAIN,
        DESSERT,
        VEGETARIAN,
        FAVOURITE,
        OWN,
        BY_NAME
    }

    init {
        downloading.value = downloadingValue

        query.value = Pair(FilterType.ALL, null)
        query.observeForever {
            //do nothing
        }
        numberOfOwnRecipes.observeForever {}

        listOfRecipes = query.switchMap {
            val query = when (it.first) {
                FilterType.STARTER -> queryMaker.getQueryForStarterRecipes()
                FilterType.MAIN -> queryMaker.getQueryForMainRecipes()
                FilterType.DESSERT -> queryMaker.getQueryForDessertRecipes()
                FilterType.VEGETARIAN -> queryMaker.getQueryForVegetarianRecipes()
                FilterType.FAVOURITE -> queryMaker.getQueryForFavouriteRecipes()
                FilterType.OWN -> queryMaker.getQueryForOwnRecipes()
                FilterType.ALL -> queryMaker.getQueryForAllRecipes()
                FilterType.BY_NAME -> it.second?.let { name -> queryMaker.getQueryForName(name) }
                    ?: queryMaker.getQueryForAllRecipes()
            }
            persistenceManager.getRecipes(query)
        }

        nextRecipeToDownloadImage = persistenceManager.getNextRecipeToDownloadImage()
        nextRecipeToDownloadImage.observeForever {
            it?.let { list ->
                if (recipesBeingDownloaded.isEmpty() && list.isNotEmpty()) {
                    viewModelScope.launch {
                        downloadPicturesFromStorage(list)
                    }
                }
            }
        }
        listOfRecipesToUpdateInServer = persistenceManager.getRecipesToUploadToServer()
        listOfRecipesToUpdateInServer.observeForever {
            it?.let { list ->
                viewModelScope.launch {
                    list.forEach { recipe ->
                        uploadRecipeToServer(recipe)
                    }
                }
            }
        }
        listOfPicturesToUpdateInServer = persistenceManager.getPicturesToUploadToServer()
        listOfPicturesToUpdateInServer.observeForever {
            it?.let { list ->
                viewModelScope.launch {
                    list.forEach { recipe ->
                        uploadPictureToServer(recipe.picture, recipe.recipeKey)
                    }
                }
            }
        }
        listOfPicturesToDeleteInServer = persistenceManager.getPicturesToDeleteInServer()
        listOfPicturesToDeleteInServer.observeForever {
            it?.let { list ->
                viewModelScope.launch {
                    list.forEach { recipe ->
                        deleteRecipeFromServer(recipe.picture, recipe.recipeKey)
                    }
                }
            }
        }
    }

    fun getListOfRecipes(): LiveData<PagedList<Recipe>> = listOfRecipes
    fun getFilterAsObservable(): MutableLiveData<Pair<FilterType, String?>> = query

    fun setFilter(filterType: FilterType, text: String? = null) {
        query.value = Pair(filterType, text)
    }

    fun downloadRecipesFromFirebase() {

        if (downloaded || downloading.value ?: 0 > 0) {
            return
        }

        viewModelScope.launch {
            subscribeToNode(FirebaseConstants.ALLOWED_RECIPES_NODE)
            subscribeToNode(FirebaseConstants.FORBIDDEN_RECIPES_NODE)

            if (preferencesManager.containsKey(PreferencesConstants.PROPERTY_FIREBASE_ID)) {
                subscribeToNode(FirebaseConstants.PERSONAL_RECIPES_NODE)
            }
        }

    }

    fun downloadingState(): LiveData<Int> = downloading

    private suspend fun subscribeToNode(refNode: String) {
        withContext(Dispatchers.Default) {
            val node: String
            val check: Int
            when (refNode) {
                FirebaseConstants.ALLOWED_RECIPES_NODE -> {
                    node = refNode
                    check = allowedRecipesCheck
                }
                FirebaseConstants.FORBIDDEN_RECIPES_NODE -> {
                    node = refNode
                    check = forbiddenRecipesCheck
                }
                FirebaseConstants.PERSONAL_RECIPES_NODE -> {
                    if (preferencesManager.containsKey(PreferencesConstants.PROPERTY_FIREBASE_ID).not()) {
                        return@withContext
                    }
                    val userId = preferencesManager.getStringFromPreferences(PreferencesConstants.PROPERTY_FIREBASE_ID)
                    node = "$refNode/$userId"
                    check = personalRecipesCheck
                }
                else -> {
                    node = refNode
                    check = 0
                }
            }

            addCheck(check)

            val mRecipeRefDetailed =
                FirebaseDatabase.getInstance().getReference("$node/${FirebaseConstants.DETAILED_RECIPES_NODE}")

            mRecipeRefDetailed.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    viewModelScope.launch {
                        downloadRecipesFromNode(dataSnapshot, check)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    removeCheck(check)
                }
            })
        }

    }

    private suspend fun downloadRecipesFromNode(dataSnapshot: DataSnapshot, check: Int) {
        withContext(Dispatchers.IO) {
            val recipes = mutableListOf<Recipe>()
            val steps = mutableListOf<Step>()
            val ingredients = mutableListOf<Ingredient>()
            dataSnapshot.children.forEach loop@{ postSnapshot ->

                postSnapshot.key?.also { key ->
                    val recipeInDb = persistenceManager.getRecipe(key)
                    val timestampFirebase = postSnapshot.getValue<TimestampFirebase>(TimestampFirebase::class.java)

                    val nodeName = dataSnapshot.ref.parent?.ref?.parent?.key
                    val recipeDownloadedOwn: Boolean =
                        !(nodeName == null || nodeName != FirebaseConstants.PERSONAL_RECIPES_NODE)
                    val recipeStoredOwn = recipeInDb?.personal == true
                    //cases to avoid storing the recipe
                    //  Firebase recipe -> personal recipe
                    //  Db recipe -> personal recipe
                    //  db timestamp >= Firebase timestamp
                    if (recipeDownloadedOwn && recipeStoredOwn &&
                        recipeInDb?.timestamp ?: Long.MIN_VALUE >= timestampFirebase?.timestamp ?: Long.MIN_VALUE
                    ) {
                        return@loop
                    }
                    //  Firebase recipe -> original recipe
                    //  Db recipe -> personal recipe
                    if (!recipeDownloadedOwn && recipeStoredOwn) {
                        return@loop
                    }
                    //  Firebase recipe -> original recipe
                    //  Db recipe -> original recipe
                    //  db timestamp >= Firebase timestamp
                    if (!recipeDownloadedOwn && recipeInDb?.timestamp ?: Long.MIN_VALUE >= timestampFirebase?.timestamp ?: Long.MIN_VALUE) {
                        return@loop
                    }
                    val recipeFromFirebase = postSnapshot.getValue(RecipeFirebase::class.java) ?: return@loop
                    recipes.add(Recipe(recipeFromFirebase, key, recipeDownloadedOwn))
                    persistenceManager.deleteIngredients(key)
                    persistenceManager.deleteSteps(key)
                    recipeFromFirebase.ingredients.forEachIndexed { index, ingredient ->
                        ingredients.add(Ingredient(recipeKey = key, position = index, ingredient = ingredient))
                    }
                    recipeFromFirebase.steps.forEachIndexed { index, step ->
                        steps.add(Step(recipeKey = key, position = index, step = step))
                    }
                }
            }
            if (steps.isNotEmpty()) {
                persistenceManager.insertSteps(steps)
            }
            if (ingredients.isNotEmpty()) {
                persistenceManager.insertIngredients(ingredients)
            }
            if (recipes.isNotEmpty()) {
                persistenceManager.insertRecipes(recipes)
            }
            removeCheck(check)
        }
    }

    private fun addCheck(check: Int) {
        if (downloadingValue.and(check) == 0) {
            downloadingValue = downloadingValue.or(check)
            downloading.postValue(downloadingValue)

        }
    }

    private fun removeCheck(check: Int) {
        if (downloading.value?.and(check) ?: 0 > 0) {
            downloadingValue = downloadingValue.xor(check)
            downloading.postValue(downloadingValue)

        }
    }

    fun isFirstLoading(): Boolean = preferencesManager.getBooleanFromPreferences(PreferencesConstants.APP_LOADED).not()

    fun setAppLoaded() {
        preferencesManager.setBooleanIntoPreferences(PreferencesConstants.APP_LOADED, true)
    }

    private suspend fun downloadPicturesFromStorage(list: List<Recipe>) {
        withContext(Dispatchers.IO) {
            list.forEach { recipe ->
                recipesBeingDownloaded.add(recipe.recipeKey)

                val storageRef = FirebaseStorage.getInstance().reference
                val imageRef: StorageReference = if (recipe.personal) {
                    if (preferencesManager.containsKey(PreferencesConstants.PROPERTY_FIREBASE_ID).not()) {
                        return@forEach
                    }
                    val userId = preferencesManager.getStringFromPreferences(PreferencesConstants.PROPERTY_FIREBASE_ID)
                    storageRef.child("personal/$userId/${recipe.picture}")
                } else {
                    storageRef.child("recipes/${recipe.picture}")
                }

                val path = readWriteUtils.getOriginalStorageDir()
                val imageFile = File(path + recipe.picture)

                imageRef.getFile(imageFile).addOnSuccessListener {
                    viewModelScope.launch(Dispatchers.Default) {
                        recipe.updatePicture = PersistenceConstants.FLAG_NOT_UPDATE_PICTURE
                        persistenceManager.setImageDownloadedInRecipe(recipe)
                        calculateColors(recipe.recipeKey, recipe.picture)
                    }
                }.addOnFailureListener {
                    viewModelScope.launch(Dispatchers.IO) {
                        if (imageFile.exists()) {
                            imageFile.delete()
                        }
                    }
                }.addOnCompleteListener {
                    recipesBeingDownloaded.remove(recipe.recipeKey)
                }
            }
        }
    }

    fun getOwnRecipes(): Boolean = numberOfOwnRecipes.value ?: 0 > 0

    private suspend fun calculateColors(recipeKey: String, pictureName: String) {

        val bitmap = viewUtils.getBitmapFromFile(pictureName)
        Palette.from(bitmap).generate {
            it?.let { palette ->
                viewModelScope.launch(Dispatchers.Default) {
                    try {
                        val mVibrantColorClear =
                            palette.getVibrantColor(resourcesManager.getColor(R.color.colorPrimary))
                        val mVibrantColorDark =
                            palette.getVibrantColor(resourcesManager.getColor(R.color.colorPrimaryRed))
                        saveColorInDisk(
                            recipeKey = recipeKey,
                            colorDark = mVibrantColorDark,
                            colorClear = mVibrantColorClear
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    private suspend fun saveColorInDisk(recipeKey: String, colorClear: Int, colorDark: Int) {
        withContext(Dispatchers.IO) {
            persistenceManager.setColorsInRecipe(
                recipeKey = recipeKey,
                colorClear = colorClear,
                colorDark = colorDark
            )
        }
    }

    private suspend fun uploadRecipeToServer(recipeWithInfo: RecipeWithInfo) {
        withContext(Dispatchers.IO) {
            if (recipesBeingUploaded.contains(recipeWithInfo.recipe.recipeKey)) {
                return@withContext
            }
            recipesBeingDownloaded.add(recipeWithInfo.recipe.recipeKey)
            val user = FirebaseAuth.getInstance().currentUser
            val ref = FirebaseDatabase
                .getInstance()
                .getReference("/${FirebaseConstants.PERSONAL_RECIPES_NODE}")
            val childUpdates = HashMap<String, Any>()


            val timestampFirebase = TimestampFirebase()
            val key = recipeWithInfo.recipe.recipeKey
            val recipeFirebase = RecipeFirebase.fromRecipeWithAllInfo(recipeWithInfo)
            val postDetailedValues = recipeFirebase.toMap()
            val postTimestamp = timestampFirebase.toMap()

            childUpdates["/" + user?.uid + "/" + FirebaseConstants.DETAILED_RECIPES_NODE + "/" + key] =
                postDetailedValues
            childUpdates["/" + user?.uid + "/" + FirebaseConstants.TIMESTAMP_RECIPES_NODE + "/" + key] =
                postTimestamp

            ref.updateChildren(childUpdates,
                DatabaseReference.CompletionListener { databaseError, _ ->
                    if (databaseError != null) {
                        Timber.d("Data could not be saved: " + databaseError.message)
                        return@CompletionListener
                    }
                    viewModelScope.launch(Dispatchers.IO) {
                        recipesBeingUploaded.remove(recipeWithInfo.recipe.recipeKey)
                        persistenceManager.setRecipeAsUploaded(recipeWithInfo.recipe.recipeKey)
                    }
                })
        }
    }

    private suspend fun uploadPictureToServer(path: String, recipeKey: String) {
        withContext(Dispatchers.IO) {
            if (picturesBeingUploaded.contains(recipeKey)) {
                return@withContext
            }
            picturesBeingUploaded.add(recipeKey)
            val user = FirebaseAuth.getInstance().currentUser

            val storageRef = FirebaseStorage.getInstance().reference
            val imageRef = storageRef.child(
                FirebaseConstants.STORAGE_PERSONAL_NODE + "/" +
                        user?.uid + "/" + path
            )

            val sFile = File(readWriteUtils.getOriginalStorageDir() + path)
            if (!sFile.exists()) {
                return@withContext
            }
            val uri = Uri.fromFile(sFile)

            val uploadTask = imageRef.putFile(uri)

            //Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener {
                Timber.d("ha fallado la subida $path")
                picturesBeingUploaded.remove(recipeKey)
            }.addOnSuccessListener {
                viewModelScope.launch(Dispatchers.IO) {
                    picturesBeingUploaded.remove(recipeKey)
                    persistenceManager.setImageDownloadedFlag(recipeKey)
                }
            }
        }
    }

    private suspend fun deleteRecipeFromServer(recipeKey: String, imageName: String) {
        withContext(Dispatchers.IO) {
            if (recipesBeingDeleted.contains(recipeKey)) {
                return@withContext
            }
            recipesBeingDeleted.add(recipeKey)
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            val ref = FirebaseDatabase
                .getInstance()
                .getReference("/" + FirebaseConstants.PERSONAL_RECIPES_NODE + "/" + uid)
            ref.child(FirebaseConstants.DETAILED_RECIPES_NODE).child(recipeKey)
                .removeValue(DatabaseReference.CompletionListener CompletionListenerLevel1@{ errorLevel1, _ ->
                    if (errorLevel1 != null) {
                        Timber.d(errorLevel1.message)
                        return@CompletionListenerLevel1
                    }
                    ref.child(FirebaseConstants.TIMESTAMP_RECIPES_NODE).child(recipeKey)
                        .removeValue(DatabaseReference.CompletionListener CompletionListenerLevel2@{ errorLevel2, _ ->
                            if (errorLevel2 != null) {
                                Timber.d(errorLevel2.message)
                                return@CompletionListenerLevel2
                            }
                            viewModelScope.launch(Dispatchers.IO) {
                                //delete picture from device
                                readWriteUtils.deleteImage(imageName)
                                //delete picture from Firebase
                                deletePictureFromFirebase(imageName)
                                //borro la receta de la base de datos
                                persistenceManager.deleteRecipe(recipeKey)
                                recipesBeingDeleted.remove(recipeKey)
                            }
                        })
                })

        }
    }

    private fun deletePictureFromFirebase(pictureName: String) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child(
            FirebaseConstants.STORAGE_PERSONAL_NODE + "/" +
                    uid + "/" + pictureName
        )

        imageRef.delete().addOnSuccessListener {
            Timber.d("borrado")
        }.addOnFailureListener {
            Timber.d("No borrado")
        }

    }

}
