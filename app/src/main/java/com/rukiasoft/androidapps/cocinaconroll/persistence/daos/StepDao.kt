package com.rukiasoft.androidapps.cocinaconroll.persistence.daos

import androidx.room.Dao
import androidx.room.Query
import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Step


/**
 * Created by Raul on 24/11/2017.
 *
 */
@Dao
abstract class StepDao : BaseDao<Step>(){

    @Query("DELETE from step WHERE recipe_key = :key")
    abstract suspend fun deleteSteps(key:String)
}