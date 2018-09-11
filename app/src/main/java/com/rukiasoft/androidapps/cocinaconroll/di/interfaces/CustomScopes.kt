package com.rukiasoft.androidapps.cocinaconroll.di.interfaces

import javax.inject.Scope


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

interface CustomScopes {

    @Scope
    annotation class ActivityScope

    @Scope
    annotation class FragmentScope

}