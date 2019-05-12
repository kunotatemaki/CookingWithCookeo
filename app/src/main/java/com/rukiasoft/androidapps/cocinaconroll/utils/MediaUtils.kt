package com.rukiasoft.androidapps.cocinaconroll.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

class MediaUtils @Inject constructor(
    val context: Context,
    val readWriteUtils: ReadWriteUtils,
    private val fileProviderUtils: FileProviderUtils
) {

    fun pickFile(screen: Any, text: String, requestCode: Int) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.type = "*/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        when (screen) {
            is Activity -> screen.startActivityForResult(Intent.createChooser(intent, text), requestCode)
            is Fragment -> screen.startActivityForResult(Intent.createChooser(intent, text), requestCode)
            else -> throw RuntimeException()
        }
    }

    fun pickPicFromGallery(screen: Any, text: String, requestCode: Int) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_PICK
        when (screen) {
            is Activity -> screen.startActivityForResult(Intent.createChooser(intent, text), requestCode)
            is Fragment -> screen.startActivityForResult(Intent.createChooser(intent, text), requestCode)
            else -> throw RuntimeException()
        }
    }

    fun pickVideoFromGallery(screen: Any, text: String, requestCode: Int) {
        val intent = Intent()
        intent.type = "video/*"
        intent.action = Intent.ACTION_PICK
        when (screen) {
            is Activity -> screen.startActivityForResult(Intent.createChooser(intent, text), requestCode)
            is Fragment -> screen.startActivityForResult(Intent.createChooser(intent, text), requestCode)
            else -> throw RuntimeException()
        }
    }

    /**
     * the uri has to be passed with the content:// scheme, rather than file://
     * If not, the app will crash on devices running MarsMallow and above.
     * See https://inthecheesefactory.com/blog/how-to-share-access-to-file-with-fileprovider-on-android-nougat/en
     * for more details
     */
    suspend fun takePicFromCamera(screen: Any, uri: Uri, requestCode: Int) {
        withContext(Dispatchers.Default) {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            launchCamera(screen, cameraIntent, requestCode)
        }
    }

    suspend fun takeVideoFromCamera(screen: Any, uri: Uri, requestCode: Int) {
        withContext(Dispatchers.Default) {
            val cameraIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            launchCamera(screen, cameraIntent, requestCode)
        }
    }

    private suspend fun launchCamera(screen: Any, cameraIntent: Intent, requestCode: Int) {
        withContext(Dispatchers.Main) {
            if (cameraIntent.resolveActivity(context.packageManager) != null) {
                when (screen) {
                    is Activity -> screen.startActivityForResult(cameraIntent, requestCode)
                    is Fragment -> screen.startActivityForResult(cameraIntent, requestCode)
                    else -> throw RuntimeException()
                }
            }
        }
    }

    suspend fun doCrop(screen: Any, imageUri: Uri, code: Int): Uri? =
        withContext(Dispatchers.IO) {
            val cropIntent = Intent("com.android.camera.action.CROP")
            //indicate image type and Uri
            val convertedUri = fileProviderUtils.getConvertedUri(imageUri)
            cropIntent.setDataAndType(convertedUri, "image/*")
            cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            cropIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)

            //set crop properties
            cropIntent.putExtra("crop", "true")
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 4)
            cropIntent.putExtra("aspectY", 3)
            //indicate output X and Y
            cropIntent.putExtra("outputX", 200)
            cropIntent.putExtra("outputY", 150)
            //retrieve data on return
            cropIntent.putExtra("return-data", false)
            val cropUri =
                Uri.fromFile(
                    readWriteUtils.createImageFile(GeneralConstants.TEMP_CROP_NAME)
                )

            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri)
            //start the activity - we handle returning in onActivityResult
            when (screen) {
                is Activity -> screen.startActivityForResult(cropIntent, code)
                is Fragment -> screen.startActivityForResult(cropIntent, code)
                else -> throw RuntimeException()
            }
            cropUri
        }

}