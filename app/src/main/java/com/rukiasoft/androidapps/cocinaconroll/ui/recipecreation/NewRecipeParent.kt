package com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation

import androidx.lifecycle.LiveData
import com.rukiasoft.androidapps.cocinaconroll.persistence.relations.RecipeWithInfo


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

interface NewRecipeParent{
    enum class ChildPosition(val position: Int){
        FIRST(0), SECOND(1), THIRD(2)
    }
    fun setFragmentSelected(childPosition: ChildPosition)
    fun getRecipe(): LiveData<RecipeWithInfo>

}