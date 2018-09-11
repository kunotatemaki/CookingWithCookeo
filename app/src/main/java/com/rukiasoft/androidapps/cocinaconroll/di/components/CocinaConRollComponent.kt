package com.rukiasoft.androidapps.cocinaconroll.di.components

import com.rukiasoft.androidapps.cocinaconroll.di.modules.ActivityBuilder
import com.rukiasoft.androidapps.cocinaconroll.di.modules.FragmentsProvider
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


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

@Singleton
@Component(modules = [(AndroidSupportInjectionModule::class),
    (ActivityBuilder::class), (CocinaConRollModule::class),
    (FragmentsProvider::class), (FragmentsProvider::class),
    (ViewModelModule::class)])
interface CodeWarsComponent : AndroidInjector<CocinaConRollApplicationBase> {

    override fun inject(fireflyApp: CocinaConRollApplicationBase)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: CocinaConRollApplicationBase): Builder

        fun build(): CodeWarsComponent
    }

}