package com.rukiasoft.androidapps.cocinaconroll.ui.common

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.rukiasoft.androidapps.cocinaconroll.firebase.FirebaseConstants
import com.rukiasoft.androidapps.cocinaconroll.firebase.models.RecipeFirebase
import com.rukiasoft.androidapps.cocinaconroll.firebase.models.TimestampFirebase
import com.rukiasoft.androidapps.cocinaconroll.persistence.PersistenceManager
import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Recipe
import com.rukiasoft.androidapps.cocinaconroll.preferences.PreferencesConstants
import com.rukiasoft.androidapps.cocinaconroll.preferences.PreferencesManager
import com.rukiasoft.androidapps.cocinaconroll.resources.ResourcesManager
import com.rukiasoft.androidapps.cocinaconroll.utils.AppExecutors
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
    private val resourcesManager: ResourcesManager,
    private val preferencesManager: PreferencesManager,
    private val persistenceManager: PersistenceManager,
    private val appExecutors: AppExecutors
) : ViewModel() {

    private val allowedRecipesCheck = 1
    private val forbiddenRecipesCheck = 2
    private val personalRecipesCheck = 4

    private var downloaded = false
    private val downloading: MutableLiveData<Int> = MutableLiveData()

    init {
        downloading.value = 0
    }

    fun downloadRecipesFromFirebase() {

        if (downloaded || downloading.value ?: 0 > 0) {
            return
        }

        downloadNode(FirebaseConstants.ALLOWED_RECIPES_NODE)
        downloadNode(FirebaseConstants.FORBIDDEN_RECIPES_NODE)

        val isSigned = preferencesManager.getBooleanFromPreferences(PreferencesConstants.PROPERTY_SIGNED_IN)
        if (isSigned) {
            downloadNode(FirebaseConstants.PERSONAL_RECIPES_NODE)
        }

    }

    fun downloadingState(): LiveData<Int> = downloading

    private fun downloadNode(node: String) {
        val check = when (node) {
            FirebaseConstants.ALLOWED_RECIPES_NODE -> allowedRecipesCheck
            FirebaseConstants.FORBIDDEN_RECIPES_NODE -> forbiddenRecipesCheck
            FirebaseConstants.PERSONAL_RECIPES_NODE -> {
                //todo mirar lo del login
//                val user = FirebaseAuth.getInstance().getCurrentUser() ?: return
//                node += "/" + user.getUid()
                personalRecipesCheck
            }
            else -> 0
        }

        addCheck(check)


        val mRecipeRefDetailed =
            FirebaseDatabase.getInstance().getReference("$node/${FirebaseConstants.DETAILED_RECIPES_NODE}")
        mRecipeRefDetailed.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                appExecutors.networkIO().execute {
                    downloadInBackground(dataSnapshot)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                removeCheck(check)
            }
        })
    }

    @WorkerThread
    private fun downloadInBackground(dataSnapshot: DataSnapshot) {
        val recipes = mutableListOf<Recipe>()
        dataSnapshot.children.forEach loop@{ postSnapshot ->

            postSnapshot.key?.let { key ->
                val recipeInDb = persistenceManager.getRecipe(key)
                val timestampFirebase = postSnapshot.getValue<TimestampFirebase>(TimestampFirebase::class.java)

                val nodeName = dataSnapshot.ref.parent?.ref?.parent?.key
                val recipeDownloadedOwn: Boolean =
                    !(nodeName == null || nodeName != FirebaseConstants.PERSONAL_RECIPES_NODE)
                val recipeStoredOwn =
                    (recipeInDb?.owner == FirebaseConstants.FLAG_PERSONAL_RECIPE).and(recipeInDb?.edited ?: false)
                //cases to avoid storing the recipe
                //  Firebase recipe -> personal recipe
                //  Db recipe -> personal recipa
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
                //  Db recipe -> original recipr
                //  db timestamp >= Firebase timestamp
                if (!recipeDownloadedOwn && recipeInDb?.timestamp ?: Long.MIN_VALUE >= timestampFirebase?.timestamp ?: Long.MIN_VALUE) {
                    return@loop
                }
                val recipeFromFirebase = postSnapshot.getValue(RecipeFirebase::class.java) ?: return@loop
                recipes.add(Recipe(recipeFromFirebase, key, 99)) //todo mirar lo del owner
            }
        }
        if (recipes.isNotEmpty()) {
            persistenceManager.insertRecipes(recipes)
        }
    }

    private fun addCheck(check: Int) {
        if (downloading.value?.and(check) == 0) {
            downloading.value = downloading.value?.or(check)
        }
    }

    private fun removeCheck(check: Int) {
        if (downloading.value?.and(check) ?: 0 > 0) {
            downloading.value = downloading.value?.xor(check)
        }
    }
}