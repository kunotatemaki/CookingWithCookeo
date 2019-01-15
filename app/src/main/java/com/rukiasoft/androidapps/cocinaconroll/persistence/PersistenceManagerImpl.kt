package com.rukiasoft.androidapps.cocinaconroll.persistence

import androidx.lifecycle.LiveData
import com.rukiasoft.androidapps.cocinaconroll.persistence.databases.CookeoDatabase
import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Ingredient
import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Recipe
import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Step
import com.rukiasoft.androidapps.cocinaconroll.persistence.relations.RecipeWithInfo
import javax.inject.Inject

class PersistenceManagerImpl @Inject constructor(private val db: CookeoDatabase): PersistenceManager{

    override fun getRecipe(key: String): Recipe? =
        db.recipeDao().getRecipe(key)

    //todo pagination
    override fun getAllRecipes(): LiveData<List<RecipeWithInfo>> =
        db.recipeDao().getAllRecipes()

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

}