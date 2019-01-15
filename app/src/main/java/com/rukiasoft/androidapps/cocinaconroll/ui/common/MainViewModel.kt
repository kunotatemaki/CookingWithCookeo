package com.rukiasoft.androidapps.cocinaconroll.ui.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rukiasoft.androidapps.cocinaconroll.R
import com.rukiasoft.androidapps.cocinaconroll.firebase.FirebaseConstants
import com.rukiasoft.androidapps.cocinaconroll.preferences.PreferencesManager
import com.rukiasoft.androidapps.cocinaconroll.resources.ResourcesManager
import timber.log.Timber
import javax.inject.Inject


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

class MainViewModel @Inject constructor(
    private val resourcesManager: ResourcesManager,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private var downloaded = false
    private val downloading: MutableLiveData<Int> = MutableLiveData()

    init {
        downloading.value = 0
    }

    fun downloadRecipesFromFirebase() {
        if (downloaded || downloading.value ?: 0 > 0) {
            return
        }

        downloadRecipesInBackground(FirebaseConstants.ALLOWED_RECIPES_NODE)
        downloadRecipesInBackground(FirebaseConstants.FORBIDDEN_RECIPES_NODE)
        Timber.d("cretino ${resourcesManager.getString(R.string.app_name)}")
//        val isSigned = mTools.getBooleanFromPreferences(
//            application.getApplicationContext(),
//            RecetasCookeoConstants.PROPERTY_SIGNED_IN
//        )
//        if (isSigned) {
//            numNodesOnInitDatabase++
//            downloadRecipesOnFirstLoad(RecetasCookeoConstants.PERSONAL_RECIPES_NODE)
//        }
    }

    fun downloadingState(): LiveData<Int> = downloading

    private fun downloadRecipesInBackground(node: String) {
//        if (downloading.value ?: 0 > 0) {
//            return
//        }
//
//        numNodesOnInitDatabase = 2
//        downloadRecipesOnFirstLoad(RecetasCookeoConstants.ALLOWED_RECIPES_NODE)
//        downloadRecipesOnFirstLoad(RecetasCookeoConstants.FORBIDDEN_RECIPES_NODE)
//        val isSigned = mTools.getBooleanFromPreferences(
//            application.getApplicationContext(),
//            RecetasCookeoConstants.PROPERTY_SIGNED_IN
//        )
//        if (isSigned) {
//            numNodesOnInitDatabase++
//            downloadRecipesOnFirstLoad(RecetasCookeoConstants.PERSONAL_RECIPES_NODE)
//        }
//
//        //descargo las recetas
//        if (node == RecetasCookeoConstants.PERSONAL_RECIPES_NODE)
//{
//val user = FirebaseAuth.getInstance().getCurrentUser() ?: return
//    node += "/" + user!!.getUid()
//}
//        val mRecipeRefDetailed = FirebaseDatabase.getInstance()
//.getReference(
//    node +
//    "/" + RecetasCookeoConstants.DETAILED_RECIPES_NODE
//)
//        mRecipeRefDetailed.addListenerForSingleValueEvent(object: ValueEventListener {
//override fun onDataChange(dataSnapshot: DataSnapshot) {
//val downloadTask = DownloadRecipesOnFirsLoadTask()
//downloadTask.execute(dataSnapshot)
//}
//
//override fun onCancelled(databaseError: DatabaseError) {
//numNodesOnInitDatabase--
//}
//})
    }
}