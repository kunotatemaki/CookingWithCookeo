package com.rukiasoft.androidapps.cocinaconroll.persistence

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.sqlite.db.SupportSQLiteQuery
import com.rukiasoft.androidapps.cocinaconroll.persistence.databases.CookeoDatabase
import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Ingredient
import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Recipe
import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Step
import com.rukiasoft.androidapps.cocinaconroll.persistence.relations.RecipeWithInfo
import javax.inject.Inject

class PersistenceManagerImpl @Inject constructor(private val db: CookeoDatabase) : PersistenceManager {

    companion object {
        private const val PAGE_SIZE = 30
    }

    override suspend fun getRecipe(key: String): Recipe? =
        db.recipeDao().getRecipe(key)

    override suspend fun getRecipeWithAllInfo(key: String): RecipeWithInfo? =
        db.recipeDao().getRecipeWithAllInfo(key)

    override fun getRecipeAsObservable(key: String): LiveData<RecipeWithInfo> =
        db.recipeDao().getRecipeAsObservable(key)


    override fun getRecipes(query: SupportSQLiteQuery) =
        LivePagedListBuilder(db.recipeDao().getAllRecipesFromRawQuery(query), PAGE_SIZE)
            .build()

    override suspend fun insertRecipes(recipes: List<Recipe>) {
        db.recipeDao().insert(recipes)
    }

    override suspend fun insertIngredients(ingredients: List<Ingredient>?) {
        ingredients?.let {
            db.ingredientDao().insert(ingredients)
        }
    }

    override suspend fun insertSteps(steps: List<Step>?) {
        steps?.let {
            db.stepDao().insert(steps)
        }
    }

    override suspend fun deleteIngredients(key: String) {
        db.ingredientDao().deleteIngredients(key)
    }

    override suspend fun deleteSteps(key: String) {
        db.stepDao().deleteSteps(key)
    }

    override fun getNextRecipeToDownloadImage(): LiveData<List<Recipe>> =
        db.recipeDao().getNextRecipeToDownloadImage()

    override suspend fun setImageDownloadedInRecipe(recipe: Recipe) {
        db.recipeDao().update(recipe)
    }

    override fun numberOfOwnRecipes(): LiveData<Int> = db.recipeDao().numberOfOwnRecipes()

    override suspend fun setFavourite(recipeKey: String, favourite: Boolean) {
        db.recipeDao().setFavourite(recipeKey, favourite)
    }

    override suspend fun setColorsInRecipe(recipeKey: String, colorClear: Int, colorDark: Int) {
        db.recipeDao().setColorsInRecipe(recipeKey = recipeKey, colorClear = colorClear, colorDark = colorDark)
    }

    override fun getRecipesToUploadToServer(): LiveData<List<RecipeWithInfo>> =
        db.recipeDao().getRecipesToUploadToServer()

    override suspend fun setRecipeAsUploaded(recipeKey: String) {
        db.recipeDao().setRecipeAsUploaded(recipeKey)
    }

    override fun getPicturesToUploadToServer(): LiveData<List<Recipe>> =
        db.recipeDao().getPicturesToUploadToServer()

    override suspend fun setImageDownloadedFlag(recipeKey: String, flag: Int) {
        db.recipeDao().setImageDownloadedFlag(recipeKey, flag)
    }
}