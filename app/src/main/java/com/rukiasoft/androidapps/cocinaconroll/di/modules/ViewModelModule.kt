package com.rukiasoft.androidapps.cocinaconroll.di.modules

import androidx.lifecycle.ViewModelProvider
import com.rukiasoft.androidapps.cocinaconroll.di.interfaces.ViewModelKey
import com.rukiasoft.androidapps.cocinaconroll.viewmodel.CodeWarsViewModelFactory
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

    /*@Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    internal abstract fun bindSearchViewModel(searchViewModel: SearchViewModel): ViewModel
*/


    @Binds
    internal abstract fun bindViewModelFactory(factory: CodeWarsViewModelFactory): ViewModelProvider.Factory
}