package com.rukiasoft.androidapps.cocinaconroll.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.fragment.app.Fragment
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

class MediaUtils @Inject constructor(val context: Context) {

    fun pickFile(screen: Any, text: String, requestCode: Int) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.type = "*/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        when (screen){
            is Activity -> screen.startActivityForResult(Intent.createChooser(intent, text), requestCode)
            is Fragment -> screen.startActivityForResult(Intent.createChooser(intent, text), requestCode)
            else -> throw RuntimeException()
        }
    }

    fun pickPicFromGallery(screen: Any, text: String, requestCode: Int) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_PICK
        when (screen){
            is Activity -> screen.startActivityForResult(Intent.createChooser(intent, text), requestCode)
            is Fragment -> screen.startActivityForResult(Intent.createChooser(intent, text), requestCode)
            else -> throw RuntimeException()
        }
    }

    fun pickVideoFromGallery(screen: Any, text: String, requestCode: Int) {
        val intent = Intent()
        intent.type = "video/*"
        intent.action = Intent.ACTION_PICK
        when (screen){
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
    fun takePicFromCamera(screen: Any, uri: Uri, requestCode: Int) {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        launchCamera(screen, cameraIntent, requestCode)
    }

    fun takeVideoFromCamera(screen: Any, uri: Uri, requestCode: Int) {
        val cameraIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        launchCamera(screen, cameraIntent, requestCode)
    }

    private fun launchCamera(screen: Any, cameraIntent: Intent, requestCode: Int) {
        if (cameraIntent.resolveActivity(context.packageManager) != null) {
            when (screen){
                is Activity -> screen.startActivityForResult(cameraIntent, requestCode)
                is Fragment -> screen.startActivityForResult(cameraIntent, requestCode)
                else -> throw RuntimeException()
            }
        }
    }

    private fun launchCamera(fragment: Fragment, cameraIntent: Intent, requestCode: Int) {
        if (cameraIntent.resolveActivity(context.packageManager) != null) {
            fragment.startActivityForResult(cameraIntent, requestCode)
        }
    }

}