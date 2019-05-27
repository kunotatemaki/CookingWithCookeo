package com.rukiasoft.androidapps.cocinaconroll.persistence

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.sqlite.db.SupportSQLiteQuery
import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Ingredient
import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Recipe
import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Step
import com.rukiasoft.androidapps.cocinaconroll.persistence.relations.RecipeWithInfo

interface PersistenceManager {

    suspend fun getRecipe(key: String): Recipe?
    suspend fun getRecipeWithAllInfo(key: String): RecipeWithInfo?
    fun getRecipeAsObservable(key: String): LiveData<RecipeWithInfo>
    fun getRecipes(query: SupportSQLiteQuery): LiveData<PagedList<Recipe>>

    suspend fun insertRecipes(recipes: List<Recipe>)
    suspend fun insertIngredients(ingredients: List<Ingredient>?)
    suspend fun insertSteps(steps: List<Step>?)

    suspend fun deleteSteps(key: String)
    suspend fun deleteIngredients(key: String)
    fun getNextRecipeToDownloadImage(): LiveData<List<Recipe>>
    suspend fun setImageDownloadedInRecipe(recipe: Recipe)
    fun numberOfOwnRecipes(): LiveData<Int>
    suspend fun setFavourite(recipeKey: String, favourite: Boolean)
    suspend fun setColorsInRecipe(recipeKey: String, colorClear: Int, colorDark: Int)

}