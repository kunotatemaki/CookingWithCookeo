package com.rukiasoft.androidapps.cocinaconroll.ui.recipedetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.rukiasoft.androidapps.cocinaconroll.extensions.switchMap
import com.rukiasoft.androidapps.cocinaconroll.persistence.PersistenceManager
import com.rukiasoft.androidapps.cocinaconroll.persistence.relations.RecipeWithInfo
import com.rukiasoft.androidapps.cocinaconroll.utils.AbsentLiveData
import javax.inject.Inject

class RecipeDetailsViewModel @Inject constructor(
    persistenceManager: PersistenceManager
) : ViewModel() {

    private val trigger: MutableLiveData<String> = MutableLiveData()
    private val recipe: LiveData<RecipeWithInfo>

    init {
        recipe = trigger.switchMap {key->
            if(key == null){
                AbsentLiveData.create()
            }else{
                persistenceManager.getRecipeAsObservable(key)
            }
        }
    }

    fun loadRecipeFromDb(recipeKey: String){
        trigger.value = recipeKey
    }

    fun getRecipe() = recipe

    fun getRecipeAsString(recipe: RecipeWithInfo): String{
        return Gson().toJson(recipe)
    }
}
