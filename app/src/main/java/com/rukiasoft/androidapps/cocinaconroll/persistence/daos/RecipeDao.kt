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
abstract class RecipeDao : BaseDao<Recipe>() {

    @Query("SELECT * FROM recipe WHERE recipe_key = :key")
    abstract suspend fun getRecipe(key: String): Recipe

    @Transaction
    @Query("SELECT * FROM recipe WHERE recipe_key = :key")
    abstract suspend fun getRecipeWithAllInfo(key: String): RecipeWithInfo

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

    @Query("UPDATE recipe SET favourite = :favourite WHERE recipe_key = :recipeKey")
    abstract fun setFavourite(recipeKey: String, favourite: Boolean)

    @Query("UPDATE recipe SET color_clear = :colorClear, color_dark = :colorDark WHERE recipe_key = :recipeKey")
    abstract fun setColorsInRecipe(recipeKey: String, colorClear: Int, colorDark: Int)

    @Query("SELECT * FROM recipe WHERE update_recipe = ${PersistenceConstants.FLAG_UPLOAD_RECIPE}")
    abstract fun getRecipesToUploadToServer(): LiveData<List<RecipeWithInfo>>

    @Query("UPDATE recipe SET update_recipe = ${PersistenceConstants.FLAG_NOT_UPDATE_RECIPE} WHERE recipe_key = :recipeKey")
    abstract fun setRecipeAsUploaded(recipeKey: String)

    @Query("SELECT update_recipe FROM recipe WHERE recipe_key = :recipeKey")
    abstract fun getRecipeUploadToServerFlag(recipeKey: String): Int

    @Query("SELECT * FROM recipe WHERE update_recipe = ${PersistenceConstants.FLAG_UPLOAD_PICTURE}")
    abstract fun getPicturesToUploadToServer(): LiveData<List<Recipe>>

    @Query("UPDATE recipe SET update_picture = :flag WHERE recipe_key = :recipeKey")
    abstract fun setImageDownloadedFlag(recipeKey: String, flag: Int)


}