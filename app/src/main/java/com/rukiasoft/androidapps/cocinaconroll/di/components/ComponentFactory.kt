package com.rukiasoft.androidapps.cocinaconroll.di.components

import com.rukiasoft.androidapps.cocinaconroll.CocinaConRollApplication


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

object ComponentFactory {

    fun component(context: CocinaConRollApplication): CocinaConRollComponent {
        return DaggerCocinaConRollComponent.builder()
                .application(context)
                .build()
    }

}