package com.rukiasoft.androidapps.cocinaconroll.ui.signin

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.rukiasoft.androidapps.cocinaconroll.R
import com.rukiasoft.androidapps.cocinaconroll.preferences.PreferencesConstants
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

class SignInViewModel @Inject constructor(
    private val context: Context,
    private val resourcesManager: ResourcesManager,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private lateinit var googleApiClient: GoogleApiClient

    fun initializeConnection(activity: FragmentActivity, listener: GoogleApiClient.OnConnectionFailedListener) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(resourcesManager.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleApiClient = GoogleApiClient.Builder(context)
            .enableAutoManage(activity, listener)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()

    }

    fun pause(activity: FragmentActivity?) {
        activity?.let {
            googleApiClient.stopAutoManage(it)
            googleApiClient.disconnect()
        }
    }

    fun handleSignInResult(user: FirebaseUser?) {

        if (user?.isAnonymous != true) {
            preferencesManager.setStringIntoPreferences(PreferencesConstants.PROPERTY_DEVICE_OWNER_NAME, "")
            preferencesManager.setStringIntoPreferences(PreferencesConstants.PROPERTY_DEVICE_OWNER_EMAIL, "")
            preferencesManager.setStringIntoPreferences(PreferencesConstants.PROPERTY_FIREBASE_ID, "")

        } else {
            preferencesManager.setStringIntoPreferences(
                PreferencesConstants.PROPERTY_DEVICE_OWNER_NAME,
                user.displayName
            )
            preferencesManager.setStringIntoPreferences(PreferencesConstants.PROPERTY_DEVICE_OWNER_EMAIL, user.email)
            preferencesManager.setStringIntoPreferences(PreferencesConstants.PROPERTY_FIREBASE_ID, user.uid)
        }
    }

    fun revokeAccess() {

        // Firebase sign out
        FirebaseAuth.getInstance().signOut()

        // Google revoke access
        try {
            Auth.GoogleSignInApi.revokeAccess(googleApiClient)
        } catch (e: IllegalStateException) {
            Timber.e(e.message)
        }

    }

    fun getGoogleApiClient(): GoogleApiClient = googleApiClient
}