package com.rukiasoft.androidapps.cocinaconroll.utils

import android.annotation.SuppressLint
import android.content.Context
import com.rukiasoft.androidapps.cocinaconroll.persistence.utils.PersistenceConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


/**
 * Copyright (C) Rukiasoft - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Roll <raulfeliz@gmail.com>, March 2019
 *
 *
 */

class ReadWriteUtils @Inject constructor(private val context: Context) {

    fun getOriginalStorageDir(): String {
        val path =
            "${context.getExternalFilesDir(null)?.toString()}${File.separatorChar}${PersistenceConstants.RECIPES_DIR}${File.separatorChar}"
        val file = File(path)
        if (file.exists().not()) {
            file.mkdirs()
        }
        return path
    }

    @SuppressLint("SimpleDateFormat")
    suspend fun createImageFile(imageFileName: String): File? = withContext(Dispatchers.IO) {
        // Create an image file name
        val suffix = ".jpg"
        File("${getOriginalStorageDir()}$imageFileName$suffix")
    }

    fun getPersonalImageName(): String {
        val c = Calendar.getInstance()
        val df = SimpleDateFormat(
            PersistenceConstants.FORMAT_DATE_TIME,
            Locale.getDefault()
        )
        return df.format(c.time)
    }

}