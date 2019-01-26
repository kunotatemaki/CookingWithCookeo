package com.rukiasoft.androidapps.cocinaconroll.utils

import android.content.Context
import javax.inject.Inject


/**
 * Copyright (C) Rukiasoft - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Roll <raulfeliz@gmail.com>, January 2019
 *
 *
 */

class GeneralUtils @Inject constructor(private val context: Context) {

    fun getApplicationName(): String {
        val stringId = context.applicationInfo.labelRes
        return context.getString(stringId)
    }
}