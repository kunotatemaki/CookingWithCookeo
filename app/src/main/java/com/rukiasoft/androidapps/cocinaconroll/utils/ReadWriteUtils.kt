package com.rukiasoft.androidapps.cocinaconroll.utils

import android.content.Context
import android.net.Uri
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.signature.MediaStoreSignature
import com.rukiasoft.androidapps.cocinaconroll.persistence.utils.PersistenceConstants
import java.io.File
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

class ReadWriteUtils @Inject constructor(private val context: Context){

    fun getOriginalStorageDir(): String {
        val path = "${context.getExternalFilesDir(null)?.toString()}${File.separatorChar}${PersistenceConstants.RECIPES_DIR}${File.separatorChar}"
        val file = File(path)
        if (file.exists().not()) {
            file.mkdirs()
        }
        return path
    }

}