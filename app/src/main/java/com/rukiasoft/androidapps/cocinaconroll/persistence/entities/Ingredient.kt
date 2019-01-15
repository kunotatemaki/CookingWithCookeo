package com.rukiasoft.androidapps.cocinaconroll.persistence.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "ingredient",
    primaryKeys = ["recipe_key", "position"],
    indices = [(Index(value = arrayOf("recipe_key")))]
)
class Ingredient constructor(

    @ColumnInfo(name = "recipe_key")
    val recipeKey: String,
    @ColumnInfo(name = "position")
    val position: Int,
    @ColumnInfo(name = "ingredient")
    val ingredient: String
)
