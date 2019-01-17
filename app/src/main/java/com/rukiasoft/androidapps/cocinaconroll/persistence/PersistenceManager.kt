package com.rukiasoft.androidapps.cocinaconroll.persistence

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.sqlite.db.SupportSQLiteQuery
import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Ingredient
import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Recipe
import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Step
import com.rukiasoft.androidapps.cocinaconroll.persistence.relations.RecipeWithInfo

interface PersistenceManager {

    fun getRecipe(key: String): Recipe?
    fun getRecipeAsObservable(key: String): LiveData<RecipeWithInfo>
    fun getRecipes(query: SupportSQLiteQuery): LiveData<PagedList<Recipe>>

    fun insertRecipes(recipes: List<Recipe>)
    fun insertIngredients(ingredients: List<Ingredient>)
    fun insertSteps(steps: List<Step>)

    fun deleteSteps(key: String)
    fun deleteIngredients(key: String)
}