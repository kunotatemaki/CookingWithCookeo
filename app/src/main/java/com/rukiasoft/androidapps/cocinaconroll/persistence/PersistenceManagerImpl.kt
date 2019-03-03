package com.rukiasoft.androidapps.cocinaconroll.persistence

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    override fun getRecipe(key: String): Recipe? =
        db.recipeDao().getRecipe(key)

    override fun getRecipeAsObservable(key: String): LiveData<RecipeWithInfo> =
        db.recipeDao().getRecipeAsObservable(key)


    override fun getRecipes(query: SupportSQLiteQuery) =
        LivePagedListBuilder(db.recipeDao().getAllRecipesFromRawQuery(query), PAGE_SIZE)
            .build()

    override fun insertRecipes(recipes: List<Recipe>) {
        db.recipeDao().insert(recipes)
    }

    override fun insertIngredients(ingredients: List<Ingredient>) {
        db.ingredientDao().insert(ingredients)
    }

    override fun insertSteps(steps: List<Step>) {
        db.stepDao().insert(steps)
    }

    override fun deleteIngredients(key: String) {
        db.ingredientDao().deleteIngredients(key)
    }

    override fun deleteSteps(key: String) {
        db.stepDao().deleteSteps(key)
    }

    override fun getNextRecipeToDownloadImage(): LiveData<List<Recipe>> =
        db.recipeDao().getNextRecipeToDownloadImage()

    override fun setImageDownloadedInRecipe(recipe: Recipe){
        db.recipeDao().update(recipe)
    }

    override fun numberOfOwnRecipes(): LiveData<Int> = db.recipeDao().numberOfOwnRecipes()

}