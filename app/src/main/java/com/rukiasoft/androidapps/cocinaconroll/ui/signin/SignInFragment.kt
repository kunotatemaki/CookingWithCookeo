package com.rukiasoft.androidapps.cocinaconroll.ui.signin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.rukiasoft.androidapps.cocinaconroll.R
import com.rukiasoft.androidapps.cocinaconroll.databinding.SigningFragmentBinding
import com.rukiasoft.androidapps.cocinaconroll.preferences.PreferencesConstants
import com.rukiasoft.androidapps.cocinaconroll.ui.common.BaseFragment
import com.rukiasoft.androidapps.cocinaconroll.ui.common.MainActivity
import timber.log.Timber


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

class SignInFragment : BaseFragment(), GoogleApiClient.OnConnectionFailedListener {
    companion object {
        const val REQUEST_CODE_GOOGLE_SIGN_IN: Int = 12345
    }

    private lateinit var viewModel: SignInViewModel

    private lateinit var binding: SigningFragmentBinding

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.signing_fragment, container, false, cookeoBindingComponent)


        // Set up button click listeners
        binding.buttonsSigning.signInButton.setOnClickListener {
            signIn()
        }
        binding.buttonsSigning.signInAnonymousButton.setOnClickListener {
            activity?.onBackPressed()
        }

        // Large sign-in
        binding.buttonsSigning.signInButton.setSize(SignInButton.SIZE_WIDE)


        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SignInViewModel::class.java)
        (activity as? MainActivity)?.setToolbar(
            binding.signInToolbar.standardToolbar,
            false,
            resourcesManager.getString(R.string.sign_in)
        )
        activity?.let {
            viewModel.initializeConnection(it, this)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == REQUEST_CODE_GOOGLE_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                // Google Sign In was successful, authenticate with Firebase
                if (result.signInAccount == null) {
                    (activity as? MainActivity)?.hideLoading()
                } else {
                    result.signInAccount?.let { account ->
                        firebaseAuthWithGoogle(account)
                    }
                }
            } else {
                // Google Sign In failed, update UI appropriately
                preferenceManager.setBooleanIntoPreferences(PreferencesConstants.PROPERTY_SIGNED_IN, false)
//                Toast.makeText(getApplicationContext(), getString(R.string.signed_in_err), Toast.LENGTH_LONG)
                viewModel.revokeAccess()
                enableButtons()
                (activity as? MainActivity)?.hideLoading()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause(activity)

    }

    // [START signIn]
    private fun signIn() {
        (activity as? MainActivity)?.showLoading()
        disableButtons()
//        showProgressDialog(getString(R.string.signing_in))
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(viewModel.getGoogleApiClient())
        this.startActivityForResult(signInIntent, REQUEST_CODE_GOOGLE_SIGN_IN)
    }


    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Timber.d("firebaseAuthWithGoogle: ${acct.id}")
//        showProgressDialog(getString(R.string.signing_in))
        activity?.let {
            val credential = GoogleAuthProvider.getCredential(acct.idToken, null)

            auth.signInWithCredential(credential)
                .addOnCompleteListener(it) { task ->
                    (activity as? MainActivity)?.hideLoading()
                    if (task.isSuccessful) {
                        val user = FirebaseAuth.getInstance().currentUser
                        viewModel.handleSignInResult(user)
                        preferenceManager.setBooleanIntoPreferences(PreferencesConstants.PROPERTY_SIGNED_IN, true)
                        (activity as? MainActivity)?.apply {
                            downloadRecipesFromFirebase()
                            onBackPressed()
                        }
                    } else {
                        Timber.w("signInWithCredential ${task.exception}")
                        Toast.makeText(context, getString(R.string.signed_in_err), Toast.LENGTH_SHORT).show()
                        viewModel.revokeAccess()
                        preferenceManager.setBooleanIntoPreferences(PreferencesConstants.PROPERTY_SIGNED_IN, false)
                        enableButtons()
                    }
                }
        }

    }


    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        (activity as? MainActivity)?.hideLoading()
    }

    private fun disableButtons() {
        binding.buttonsSigning.signInButton.isEnabled = false
        binding.buttonsSigning.signInAnonymousButton.isEnabled = false
    }

    private fun enableButtons() {
        binding.buttonsSigning.signInButton.isEnabled = true
        binding.buttonsSigning.signInAnonymousButton.isEnabled = true
    }
}