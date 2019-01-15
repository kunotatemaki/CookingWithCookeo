package com.rukiasoft.androidapps.cocinaconroll.di.modules

import android.content.Context
import com.rukiasoft.androidapps.cocinaconroll.CocinaConRollApplication
import com.rukiasoft.androidapps.cocinaconroll.resources.ResourcesManager
import com.rukiasoft.androidapps.cocinaconroll.resources.ResourcesManagerImpl
import dagger.Binds
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

    /*@Provides
    fun providesPersistenceManager(persistenceManager: PersistenceManagerImpl): PersistenceManager {
        return persistenceManager
    }

    @Provides
    fun providesPreferencesManager(codeWarsPreferences: CocinaConRollPreferencesImpl): CocinaConRollPreferences {
        return codeWarsPreferences
    }

    @Singleton
    @Provides
    fun provideDb(app: CocinaConRollApplicationBase, preferences: CocinaConRollPreferences): CocinaConRollDatabase {


        return Room.databaseBuilder(app,
                CodeWarsDatabase::class.java, Constants.DATABASE_NAME)
                //.addMigrations()    //no migrations, version 1
                .fallbackToDestructiveMigration()
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onOpen(db: SupportSQLiteDatabase) {
                        super.onOpen(db)
                        //if the db is updated, remove info from preferences (as we are recreating all the tables)
                        val oldVersion = preferences.getDbVersion()
                        if (oldVersion != db.version) {
                            //store the new version
                            preferences.setDbVersion(db.version)
                        }
                    }
                })
                .build()

    }*/


}