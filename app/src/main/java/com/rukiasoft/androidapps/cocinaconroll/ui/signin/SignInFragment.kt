package com.rukiasoft.androidapps.cocinaconroll.ui.signin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseUser
import com.rukiasoft.androidapps.cocinaconroll.R
import com.rukiasoft.androidapps.cocinaconroll.databinding.CookeoBindingComponent
import com.rukiasoft.androidapps.cocinaconroll.databinding.SigningFragmentBinding
import com.rukiasoft.androidapps.cocinaconroll.ui.common.BaseFragment
import com.rukiasoft.androidapps.cocinaconroll.ui.common.MainActivity
import com.rukiasoft.androidapps.cocinaconroll.ui.recipelist.RecipeListViewModel


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

    private lateinit var viewModel: SignInViewModel

    internal var mStatus: TextView? = null

    internal var signInButton: SignInButton? = null

    internal var anonymousButton: Button? = null

    private lateinit var mGoogleApiClient: GoogleApiClient
    private lateinit var binding: SigningFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding =
                DataBindingUtil.inflate(inflater, R.layout.signing_fragment, container, false, CookeoBindingComponent())


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





//
//    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
//        val tools = Tools()
//        if (requestCode == RecetasCookeoConstants.REQUEST_CODE_GOOGLE_SIGN_IN) {
//            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
//            if (result.isSuccess()) {
//                // Google Sign In was successful, authenticate with Firebase
//                val account = result.getSignInAccount()
//                firebaseAuthWithGoogle(account)
//            } else {
//                // Google Sign In failed, update UI appropriately
//                hideProgressDialog()
//                tools.savePreferences(getApplicationContext(), RecetasCookeoConstants.PROPERTY_SIGNED_IN, false)
//                Toast.makeText(getApplicationContext(), getString(R.string.signed_in_err), Toast.LENGTH_LONG)
//                revokeAccess()
//                enableButtons()
//            }
//        }
//    }

    // [START signIn]
    protected fun signIn() {
//        disableButtons()
//        showProgressDialog(getString(R.string.signing_in))
//        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
//        startActivityForResult(signInIntent, RecetasCookeoConstants.REQUEST_CODE_GOOGLE_SIGN_IN)
    }

    protected fun signInAnnonimously() {
        //Quito el acceso por si lo tenía
//        disableButtons()
//        showProgressDialog(getString(R.string.signing_in))
//        revokeAccess()
//        //Autentico anónimamente
//        FirebaseAuth.getInstance().signInAnonymously()
//            .addOnCompleteListener(this, OnCompleteListener<Any> { task ->
//                Logger.d("signInWithCredential:onComplete:" + task.isSuccessful)
//                //Para bien o para mal, pulsando aquí no está signed in (estará en fallo o anónimo)
//                val tools = Tools()
//                tools.savePreferences(getApplicationContext(), RecetasCookeoConstants.PROPERTY_SIGNED_IN, false)
//
//                hideProgressDialog()
//                // If sign in fails, display a message to the user. If sign in succeeds
//                // the auth state listener will be notified and logic to handle the
//                // signed in user can be handled in the listener.
//                if (!task.isSuccessful) {
//                    Logger.w("signInWithCredential", task.exception)
//                    Toast.makeText(
//                        this@SignInActivity, getString(R.string.signed_in_err),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    revokeAccess()
//                    enableButtons()
//                } else {
//                    val user = FirebaseAuth.getInstance().getCurrentUser()
//                    handleSignInResult(user)
//                    finish()
//                }
//            })
//            .addOnFailureListener(OnFailureListener {
//                hideProgressDialog()
//                Logger.d("no hace nada en anónimo")
//            })
    }


    protected fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
//        Logger.d("firebaseAuthWithGoogle:" + acct.id!!)
//        showProgressDialog(getString(R.string.signing_in))
//        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
//        FirebaseAuth.getInstance().signInWithCredential(credential)
//            .addOnCompleteListener(this, OnCompleteListener<Any> { task ->
//                Logger.d("signInWithCredential:onComplete:" + task.isSuccessful)
//                hideProgressDialog()
//                // If sign in fails, display a message to the user. If sign in succeeds
//                // the auth state listener will be notified and logic to handle the
//                // signed in user can be handled in the listener.
//                val tools = Tools()
//                if (!task.isSuccessful) {
//                    Logger.w("signInWithCredential", task.exception)
//                    Toast.makeText(
//                        this@SignInActivity, getString(R.string.signed_in_err),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    revokeAccess()
//                    tools.savePreferences(getApplicationContext(), RecetasCookeoConstants.PROPERTY_SIGNED_IN, false)
//                    enableButtons()
//                } else {
//                    val user = FirebaseAuth.getInstance().getCurrentUser()
//                    handleSignInResult(user)
//                    tools.savePreferences(getApplicationContext(), RecetasCookeoConstants.PROPERTY_SIGNED_IN, true)
//                    finish()
//                }
//            })
    }


    override fun onConnectionFailed(connectionResult: ConnectionResult) {

    }

    private fun handleSignInResult(user: FirebaseUser?) {
//        val mTools = Tools()
//        if (user != null && !user!!.isAnonymous()) {
//            mTools.savePreferences(this, RecetasCookeoConstants.PROPERTY_DEVICE_OWNER_NAME, user!!.getDisplayName())
//            mTools.savePreferences(this, RecetasCookeoConstants.PROPERTY_DEVICE_OWNER_EMAIL, user!!.getEmail())
//            mTools.savePreferences(this, RecetasCookeoConstants.PROPERTY_FIREBASE_ID, user!!.getUid())
//        } else {
//            mTools.savePreferences(this, RecetasCookeoConstants.PROPERTY_DEVICE_OWNER_NAME, "")
//            mTools.savePreferences(this, RecetasCookeoConstants.PROPERTY_DEVICE_OWNER_EMAIL, "")
//            mTools.savePreferences(this, RecetasCookeoConstants.PROPERTY_FIREBASE_ID, "")
//        }
    }


    protected fun revokeAccess() {

//        // Firebase sign out
//        FirebaseAuth.getInstance().signOut()
//
//        // Google revoke access
//        try {
//            Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient)
//        } catch (e: IllegalStateException) {
//            Logger.e(e.message)
//        }

    }

    private fun disableButtons() {
        if (signInButton != null) {
            signInButton!!.isEnabled = false
        }
        if (anonymousButton != null) {
            anonymousButton!!.isEnabled = false
        }
    }

    private fun enableButtons() {
        if (signInButton != null) {
            signInButton!!.isEnabled = true
        }
        if (anonymousButton != null) {
            anonymousButton!!.isEnabled = true
        }
    }
}