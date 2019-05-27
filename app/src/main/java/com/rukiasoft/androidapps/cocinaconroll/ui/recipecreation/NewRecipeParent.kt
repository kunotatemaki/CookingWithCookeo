package com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation

import androidx.lifecycle.LiveData
import com.rukiasoft.androidapps.cocinaconroll.persistence.relations.RecipeWithInfo
import com.rukiasoft.androidapps.cocinaconroll.persistence.utils.PersistenceConstants


/**
 * Copyright (C) Rukiasoft - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Roll <raulfeliz@gmail.com>, May 2019
 *
 *
 */

interface NewRecipeParent {
    enum class ChildPosition(val position: Int) {
        FIRST(0), SECOND(1), THIRD(2)
    }

    fun setFragmentSelected(childPosition: ChildPosition)
    fun getRecipe(): LiveData<RecipeWithInfo>
    fun setStep1(
        name: String,
        picture: String,
        minutes: String? = null,
        portions: String? = null,
        type: String,
        vegetarian: Boolean = false
    )
    fun setIngredients(ingredients: List<String>)
    fun saveIngredientInBox(ingredient: String)
    fun getIngredientInBox(): String
    fun setSteps(steps: List<String>)
    fun saveStepInBox(step: String)
    fun getStepInBox(): String

}