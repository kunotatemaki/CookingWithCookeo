package com.rukiasoft.androidapps.cocinaconroll

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.facebook.stetho.Stetho
import com.rukiasoft.androidapps.cocinaconroll.di.components.CocinaConRollComponent
import com.rukiasoft.androidapps.cocinaconroll.di.components.ComponentFactory
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber


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

class CocinaConRollApplication : DaggerApplication() {

    private val recipeDetailsBitmap: MutableLiveData<Bitmap> = MutableLiveData()

    override fun applicationInjector(): AndroidInjector<CocinaConRollApplication> {
        val mComponent: CocinaConRollComponent = ComponentFactory.component(this)
        mComponent.inject(this)
        return mComponent
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        recipeDetailsBitmap.value = null

        //initialize Stetho
        Stetho.initialize(
            Stetho.newInitializerBuilder(this)
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build()
        )


        // Initialize Logging with Timber
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }


        //Timber.d("db path: %s", getDatabasePath(Constants.DATABASE_NAME).absolutePath)

    }

    fun getRecipeDetailsBitmap(): LiveData<Bitmap> = recipeDetailsBitmap
    fun resetRecipeDetailsBitmap() {
        recipeDetailsBitmap.postValue(null)
    }

    fun setRecipeDetailsBitmap(bitmap: Bitmap) {
        recipeDetailsBitmap.postValue(bitmap)
    }


    /** A tree which logs important information for crash reporting. (Tiber) */
    private class CrashReportingTree : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return
            }

        }
    }

    companion object {

        private lateinit var instance: CocinaConRollApplication

        fun get(): CocinaConRollApplication {
            return instance
        }
    }


}
