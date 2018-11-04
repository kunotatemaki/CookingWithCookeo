package com.rukiasoft.androidapps.cocinaconroll.di.modules

import com.rukiasoft.androidapps.cocinaconroll.MainActivity
import com.rukiasoft.androidapps.cocinaconroll.MainActivity2
import com.rukiasoft.androidapps.cocinaconroll.di.interfaces.CustomScopes
import com.rukiasoft.androidapps.cocinaconroll.ui.animation.AnimationActivity
import com.rukiasoft.androidapps.cocinaconroll.ui.splash.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class ActivityBuilder {

    @CustomScopes.ActivityScope
    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity

    @CustomScopes.ActivityScope
    @ContributesAndroidInjector
    abstract fun bindMainActivity2(): MainActivity2

    @CustomScopes.ActivityScope
    @ContributesAndroidInjector
    abstract fun bindSplashScreen(): SplashActivity

    @CustomScopes.ActivityScope
    @ContributesAndroidInjector
    abstract fun bindAnimationScreen(): AnimationActivity

    /*@CustomScopes.ActivityScope
    @ContributesAndroidInjector
    abstract fun bindDetailsScreen(): DetailsActivity*/


}