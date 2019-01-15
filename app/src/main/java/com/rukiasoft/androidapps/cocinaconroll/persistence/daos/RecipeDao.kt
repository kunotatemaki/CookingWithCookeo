package com.rukiasoft.androidapps.cocinaconroll.persistence.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Recipe
import com.rukiasoft.androidapps.cocinaconroll.persistence.relations.RecipeWithInfo


/**
 * Created by Raul on 24/11/2017.
 *
 */
@Dao
abstract class RecipeDao : BaseDao<Recipe> {

    @Query("SELECT * FROM recipe WHERE recipe_key = :key")
    abstract fun getRecipe(key: String): Recipe

    @Query("SELECT * FROM recipe")
    abstract fun getAllRecipes(): LiveData<List<RecipeWithInfo>>




}