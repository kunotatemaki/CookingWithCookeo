package com.rukiasoft.androidapps.cocinaconroll.persistence

import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Recipe

interface PersistenceManager {

    fun getRecipe(key: String): Recipe?

    fun insertRecipes(recipes: List<Recipe>)
}