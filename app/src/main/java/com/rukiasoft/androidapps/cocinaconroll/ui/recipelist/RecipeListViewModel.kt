package com.rukiasoft.androidapps.cocinaconroll.ui.recipelist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.rukiasoft.androidapps.cocinaconroll.persistence.PersistenceManager
import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Recipe
import com.rukiasoft.androidapps.cocinaconroll.persistence.relations.RecipeWithInfo
import javax.inject.Inject

class RecipeListViewModel @Inject constructor(
    persistenceManager: PersistenceManager
) : ViewModel() {

    private val recipes: LiveData<List<Recipe>> = persistenceManager.getAllRecipes()


    fun getListOfRecipes(): LiveData<List<Recipe>> = recipes


}
