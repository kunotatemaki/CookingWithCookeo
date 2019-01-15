package com.rukiasoft.androidapps.cocinaconroll.persistence.daos

import androidx.room.Dao
import androidx.room.Query
import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Ingredient


/**
 * Created by Raul on 24/11/2017.
 *
 */
@Dao
abstract class IngredientDao : BaseDao<Ingredient>{

    @Query("DELETE from ingredient WHERE recipe_key = :key")
    abstract fun deleteIngredients(key: String)
}