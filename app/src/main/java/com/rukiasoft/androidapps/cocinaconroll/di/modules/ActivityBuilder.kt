package com.rukiasoft.androidapps.cocinaconroll.di.modules

import com.rukiasoft.androidapps.cocinaconroll.MainActivity
import com.rukiasoft.androidapps.cocinaconroll.di.interfaces.CustomScopes
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class ActivityBuilder {

    @CustomScopes.ActivityScope
    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity

    /*@CustomScopes.ActivityScope
    @ContributesAndroidInjector
    abstract fun bindChallengesScreen(): ChallengesActivity

    @CustomScopes.ActivityScope
    @ContributesAndroidInjector
    abstract fun bindDetailsScreen(): DetailsActivity*/


}