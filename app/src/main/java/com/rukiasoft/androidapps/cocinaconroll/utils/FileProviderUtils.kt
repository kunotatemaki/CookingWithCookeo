package com.rukiasoft.androidapps.cocinaconroll.utils

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import androidx.core.content.FileProvider
import com.rukiasoft.androidapps.cocinaconroll.BuildConfig
import java.io.File
import java.net.URISyntaxException
import javax.inject.Inject


/**
 * Copyright (C) Rukiasoft - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Roll <raulfeliz@gmail.com>, May 2019
 *
 *
 */

class FileProviderUtils @Inject constructor(private val context: Context) {

    fun getConvertedUri(uri: Uri): Uri {

        val authority = BuildConfig.APPLICATION_ID + ".fileprovider"

        return FileProvider.getUriForFile(context,
            authority,
            File(uri.path)
        )

    }

}
