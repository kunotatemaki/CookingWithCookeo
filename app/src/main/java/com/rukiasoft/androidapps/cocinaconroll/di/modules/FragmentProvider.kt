package com.rukiasoft.androidapps.cocinaconroll.di.modules

import com.rukiasoft.androidapps.cocinaconroll.BlankFragment
import com.rukiasoft.androidapps.cocinaconroll.di.interfaces.CustomScopes
import dagger.Module
import dagger.android.ContributesAndroidInjector


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

@Suppress("unused")
@Module
abstract class FragmentsProvider {

    @CustomScopes.FragmentScope
    @ContributesAndroidInjector
    abstract fun providesBlankFragmentFragmentFactory(): BlankFragment
}