package com.rukiasoft.androidapps.cocinaconroll.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
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

    fun getCurrentUser(): FirebaseUser? = FirebaseAuth.getInstance().currentUser

    fun getFirebaseKeyInAdvance(userUid: String? = null, node: String): String? {
        val uid = userUid ?: getCurrentUser()?.uid
        return if (uid != null) {
            val ref = FirebaseDatabase
                .getInstance()
                .getReference(node)
            ref.child(uid).push().key
        } else {
            null
        }
    }

}