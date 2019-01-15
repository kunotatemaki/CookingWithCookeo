package com.rukiasoft.androidapps.cocinaconroll.persistence

import com.rukiasoft.androidapps.cocinaconroll.persistence.databases.CookeoDatabase
import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Recipe
import javax.inject.Inject

class PersistenceManagerImpl @Inject constructor(private val db: CookeoDatabase): PersistenceManager{

    override fun getRecipe(key: String): Recipe? =
        db.recipeInfoDao().getRecipe(key)

    override fun insertRecipes(recipes: List<Recipe>) {
        db.recipeInfoDao().insert(recipes)
    }

}