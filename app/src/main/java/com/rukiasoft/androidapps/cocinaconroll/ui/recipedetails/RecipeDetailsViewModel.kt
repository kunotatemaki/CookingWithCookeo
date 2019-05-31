package com.rukiasoft.androidapps.cocinaconroll.ui.recipedetails

import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rukiasoft.androidapps.cocinaconroll.R
import com.rukiasoft.androidapps.cocinaconroll.extensions.switchMap
import com.rukiasoft.androidapps.cocinaconroll.persistence.PersistenceManager
import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Recipe
import com.rukiasoft.androidapps.cocinaconroll.persistence.relations.RecipeWithInfo
import com.rukiasoft.androidapps.cocinaconroll.resources.ResourcesManager
import com.rukiasoft.androidapps.cocinaconroll.utils.AbsentLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RecipeDetailsViewModel @Inject constructor(
    private val persistenceManager: PersistenceManager,
    private val resourcesManager: ResourcesManager
) : ViewModel() {

    private val trigger: MutableLiveData<String> = MutableLiveData()
    private val recipe: LiveData<RecipeWithInfo>

    init {
        recipe = trigger.switchMap { key ->
            if (key == null) {
                AbsentLiveData.create()
            } else {
                persistenceManager.getRecipeAsObservable(key)
            }
        }
    }

    fun loadRecipeFromDb(recipeKey: String) {
        trigger.value = recipeKey
    }

    fun getRecipe() = recipe

    suspend fun getDefaultAuthorFormatted(): String =
        withContext(viewModelScope.coroutineContext) {
            "${resourcesManager.getString(R.string.author)} ${resourcesManager.getString(R.string.default_author)}"
        }


    suspend fun getLinkAuthorFormatted(recipe: Recipe): Spanned?{
        return withContext(viewModelScope.coroutineContext) {
            val link =
                "${resourcesManager.getString(R.string.original_link)} <a href=\"${recipe.link}\">${recipe.author}</a>"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(link, Html.FROM_HTML_MODE_LEGACY)
            } else {
                @Suppress("DEPRECATION")
                Html.fromHtml(link)
            }
        }

    }

    fun deleteRecipe() {
        getRecipe().value?.let {
            viewModelScope.launch(Dispatchers.IO) {
                persistenceManager.markRecipeForDeletion(it.recipe.recipeKey)
            }
        }
    }
}
