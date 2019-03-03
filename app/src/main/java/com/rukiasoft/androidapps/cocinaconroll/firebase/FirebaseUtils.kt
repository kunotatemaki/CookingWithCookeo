package com.rukiasoft.androidapps.cocinaconroll.firebase

import javax.inject.Inject


/**
 * Copyright (C) Rukiasoft - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Roll <raulfeliz@gmail.com>, March 2019
 *
 *
 */

class FirebaseUtils @Inject constructor() {


    fun getRecipeFlagFromNodeName(node: String?): Int? = when (node) {
        FirebaseConstants.ALLOWED_RECIPES_NODE -> FirebaseConstants.FLAG_ALLOWED_RECIPE
        FirebaseConstants.FORBIDDEN_RECIPES_NODE -> FirebaseConstants.FLAG_FORBIDDEN_RECIPE
        FirebaseConstants.PERSONAL_RECIPES_NODE -> FirebaseConstants.FLAG_PERSONAL_RECIPE
        else -> null
    }


    fun getNodeNameFromRecipeFlag(flag: Int): String? = when (flag) {
        FirebaseConstants.FLAG_ALLOWED_RECIPE -> FirebaseConstants.ALLOWED_RECIPES_NODE
        FirebaseConstants.FLAG_FORBIDDEN_RECIPE -> FirebaseConstants.FORBIDDEN_RECIPES_NODE
        FirebaseConstants.FLAG_PERSONAL_RECIPE -> FirebaseConstants.PERSONAL_RECIPES_NODE
        else -> null
    }

}