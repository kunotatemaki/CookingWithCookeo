package com.rukiasoft.androidapps.cocinaconroll.persistence.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Ingredient
import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Recipe
import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Step


/**
 * Copyright (C) Rukiasoft - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Roll <raulfeliz@gmail.com>, January 2019
 *
 *
 */

class RecipeWithInfo {

    @Embedded
    lateinit var recipe: Recipe

    @Relation(parentColumn = "recipe_key", entityColumn = "recipe_key")
     var ingredients: List<Ingredient>? = mutableListOf()

    @Relation(parentColumn = "recipe_key", entityColumn = "recipe_key")
     var steps: List<Step>? = mutableListOf()

    fun hasTip() = recipe.tip.isNullOrBlank().not()

    fun toggleFavourite(): Boolean{
        recipe.favourite = recipe.favourite.not()
        return recipe.favourite
    }

}