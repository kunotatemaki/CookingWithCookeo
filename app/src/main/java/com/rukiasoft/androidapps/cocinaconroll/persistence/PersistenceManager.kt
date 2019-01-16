package com.rukiasoft.androidapps.cocinaconroll.persistence

import androidx.lifecycle.LiveData
import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Ingredient
import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Recipe
import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Step
import com.rukiasoft.androidapps.cocinaconroll.persistence.relations.RecipeWithInfo

interface PersistenceManager {

    fun getRecipe(key: String): Recipe?
    fun getRecipeAsObservable(key: String): LiveData<Recipe>
    fun getAllRecipes(): LiveData<List<Recipe>>

    fun insertRecipes(recipes: List<Recipe>)
    fun insertIngredients(ingredients: List<Ingredient>)
    fun insertSteps(steps: List<Step>)

    fun deleteSteps(key: String)
    fun deleteIngredients(key: String)
}