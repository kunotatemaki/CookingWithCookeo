package com.rukiasoft.androidapps.cocinaconroll.persistence.daos

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Recipe
import com.rukiasoft.androidapps.cocinaconroll.persistence.relations.RecipeWithInfo
import com.rukiasoft.androidapps.cocinaconroll.persistence.utils.PersistenceConstants


/**
 * Created by Raul on 24/11/2017.
 *
 */
@Dao
abstract class RecipeDao : BaseDao<Recipe> {

    @Query("SELECT * FROM recipe WHERE recipe_key = :key")
    abstract fun getRecipe(key: String): Recipe

    @Transaction
    @Query("SELECT * FROM recipe WHERE recipe_key = :key")
    abstract fun getRecipeAsObservable(key: String): LiveData<RecipeWithInfo>

    @Transaction
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @RawQuery(observedEntities = [(Recipe::class)])
    protected abstract fun getAllRecipesFromRawQueryInternal(query: SupportSQLiteQuery): DataSource.Factory<Int, Recipe>


    fun getAllRecipesFromRawQuery(query: SupportSQLiteQuery) =
        getAllRecipesFromRawQueryInternal(query)

    @Query("SELECT * FROM recipe WHERE update_picture = ${PersistenceConstants.FLAG_DOWNLOAD_PICTURE}")
    abstract fun getNextRecipeToDownloadImage(): LiveData<List<Recipe>>

    @Query("SELECT COUNT(recipe_key) FROM recipe WHERE personal = 1")
    abstract fun numberOfOwnRecipes(): LiveData<Int>


}