package com.rukiasoft.androidapps.cocinaconroll.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.rukiasoft.androidapps.cocinaconroll.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
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

    suspend fun getConvertedUri(uri: Uri): Uri =
        withContext(Dispatchers.Default) {
            val authority = BuildConfig.APPLICATION_ID + ".fileprovider"
            FileProvider.getUriForFile(
                context,
                authority,
                File(uri.path)
            )
        }

}
