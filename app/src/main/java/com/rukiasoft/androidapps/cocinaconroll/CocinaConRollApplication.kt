package com.rukiasoft.androidapps.cocinaconroll

import android.util.Log
import com.facebook.stetho.Stetho
import com.rukiasoft.androidapps.cocinaconroll.di.components.CocinaConRollComponent
import com.rukiasoft.androidapps.cocinaconroll.di.components.ComponentFactory
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric




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

    override fun applicationInjector(): AndroidInjector<CocinaConRollApplication> {
        val mComponent: CocinaConRollComponent = ComponentFactory.component(this)
        mComponent.inject(this)
        return mComponent
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        //initialize Stetho
        Stetho.initialize(
            Stetho.newInitializerBuilder(this)
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build()
        )

        Fabric.with(this, Crashlytics())
        // Initialize Logging with Timber
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }


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
