package com.rukiasoft.androidapps.cocinaconroll.persistence.databases

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rukiasoft.androidapps.cocinaconroll.persistence.daos.IngredientDao
import com.rukiasoft.androidapps.cocinaconroll.persistence.daos.RecipeDao
import com.rukiasoft.androidapps.cocinaconroll.persistence.daos.StepDao
import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Ingredient
import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Recipe
import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Step
import com.rukiasoft.androidapps.cocinaconroll.persistence.utils.DbConverters

@Database(entities = [(Recipe::class), (Step::class), (Ingredient::class)], exportSchema = false, version = 1)
@TypeConverters(DbConverters::class)
abstract class CookeoDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
    abstract fun stepDao(): StepDao
    abstract fun ingredientDao(): IngredientDao
}
