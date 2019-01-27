package com.rukiasoft.androidapps.cocinaconroll.ui.signin

import android.app.Activity
import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.rukiasoft.androidapps.cocinaconroll.R
import com.rukiasoft.androidapps.cocinaconroll.persistence.PersistenceManager
import com.rukiasoft.androidapps.cocinaconroll.resources.ResourcesManager
import com.rukiasoft.androidapps.cocinaconroll.ui.common.MainActivity
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

class SignInViewModel @Inject constructor(
    private val context: Context
    private val persistenceManager: PersistenceManager,
    private val resourcesManager: ResourcesManager
) : ViewModel(){

    private lateinit var mGoogleApiClient: GoogleApiClient

    fun initializeConnection(activity: FragmentActivity, listener: GoogleApiClient.OnConnectionFailedListener) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(resourcesManager.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleApiClient = GoogleApiClient.Builder(context)
            .enableAutoManage(activity, listener)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()

    }

}