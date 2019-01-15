package com.rukiasoft.androidapps.cocinaconroll.di.modules

import android.content.Context
import androidx.room.Room
import com.rukiasoft.androidapps.cocinaconroll.CocinaConRollApplication
import com.rukiasoft.androidapps.cocinaconroll.persistence.databases.CookeoDatabase
import com.rukiasoft.androidapps.cocinaconroll.persistence.utils.PersistenceConstants
import dagger.Module
import dagger.Provides
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

@Module(includes = [(ViewModelModule::class)])
class CocinaConRollProvidesModule {


    @Provides
    fun providesContext(application: CocinaConRollApplication): Context = application.applicationContext


    @Singleton
    @Provides
    fun provideDb(app: CocinaConRollApplication): CookeoDatabase {


        return Room.databaseBuilder(
            app,
            CookeoDatabase::class.java, PersistenceConstants.DATABASE_NAME
        )
            //.addMigrations()    //no migrations, version 1
            .fallbackToDestructiveMigration()
            .build()

    }


}