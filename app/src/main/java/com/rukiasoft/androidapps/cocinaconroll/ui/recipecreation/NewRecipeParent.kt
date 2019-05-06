package com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation


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

}