package com.rukiasoft.androidapps.cocinaconroll.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rukiasoft.androidapps.cocinaconroll.BlankViewModel
import com.rukiasoft.androidapps.cocinaconroll.di.interfaces.ViewModelKey
import com.rukiasoft.androidapps.cocinaconroll.ui.common.MainViewModel
import com.rukiasoft.androidapps.cocinaconroll.viewmodel.CocinaConRollViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


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
internal abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(BlankViewModel::class)
    internal abstract fun bindSearchViewModel(searchViewModel: BlankViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel


    @Binds
    internal abstract fun bindViewModelFactory(factory: CocinaConRollViewModelFactory): ViewModelProvider.Factory
}