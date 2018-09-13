package com.rukiasoft.androidapps.cocinaconroll.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import com.rukiasoft.androidapps.cocinaconroll.di.components.DaggerCocinaConRollComponent
import dagger.android.DaggerActivity


/**
 * Copyright (C) Rukiasoft - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Roll <raulfeliz@gmail.com>, septiembre 2018
 *
 *
 */

class SplashActivity : DaggerActivity() {

    internal var started = false
    //request constants
//    internal var mAuth: FirebaseAuth
//    internal var mAuthListener: FirebaseAuth.AuthStateListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


//        mAuth = FirebaseAuth.getInstance()
//        mAuthListener = object : FirebaseAuth.AuthStateListener() {
//            fun onAuthStateChanged(@NonNull firebaseAuth: FirebaseAuth) {
//                val user = firebaseAuth.getCurrentUser()
//                if (user != null) {
//                    launchMainScreen()
//                } else {
//                    launchSignInScreen()
//                }
//            }
//        }

        //start animation if needed
//        if (!started) {
            launchAnimation()
//        } else {
//            launchMainOrSigningScreen()
//        }
    }

//    protected fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
//        when (requestCode) {
//            RecetasCookeoConstants.REQUEST_CODE_ANIMATION -> launchMainOrSigningScreen()
//            RecetasCookeoConstants.REQUEST_CODE_SIGNING_FROM_SPLASH -> launchMainScreen()
//            else -> super.onActivityResult(requestCode, resultCode, data)
//        }
//    }

    private fun launchMainOrSigningScreen() {
//        mAuth.addAuthStateListener(mAuthListener)
    }

//    fun onStop() {
//        super.onStop()
//        if (mAuthListener != null) {
//            mAuth.removeAuthStateListener(mAuthListener)
//        }
//    }

    private fun launchAnimation() {
        val animationIntent = Intent(this, AnimationActivity::class.java)
        startActivityForResult(animationIntent, RecetasCookeoConstants.REQUEST_CODE_ANIMATION)
    }

    private fun launchMainScreen() {
//        val intent = Intent(this, RecipeListActivity::class.java)
//        startActivity(intent)
        finish()
    }

    private fun launchSignInScreen() {
//        val intent = Intent(this@SplashActivity, SignInActivity::class.java)
//        startActivityForResult(intent, RecetasCookeoConstants.REQUEST_CODE_SIGNING_FROM_SPLASH)
    }
}