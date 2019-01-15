package com.rukiasoft.androidapps.cocinaconroll.di.components

import com.rukiasoft.androidapps.cocinaconroll.CocinaConRollApplication
import com.rukiasoft.androidapps.cocinaconroll.di.modules.*
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
@Component(
    modules = [(AndroidSupportInjectionModule::class), (ActivityBuilder::class), (CocinaConRollBindsModule::class),
        (CocinaConRollProvidesModule::class), (FragmentsProvider::class), (FragmentsProvider::class), (ViewModelModule::class)]
)
interface CocinaConRollComponent : AndroidInjector<CocinaConRollApplication> {

    override fun inject(fireflyApp: CocinaConRollApplication)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: CocinaConRollApplication): Builder

        fun build(): CocinaConRollComponent
    }

}