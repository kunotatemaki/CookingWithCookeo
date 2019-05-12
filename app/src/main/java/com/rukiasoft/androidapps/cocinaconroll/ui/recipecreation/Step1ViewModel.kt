package com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rukiasoft.androidapps.cocinaconroll.utils.ReadWriteUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
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

class Step1ViewModel @Inject constructor(val readWriteUtils: ReadWriteUtils) : ViewModel() {


    private var imageNameObservable: MutableLiveData<String> = MutableLiveData()

    fun getImageName() = imageNameObservable
    fun setImageName(imageName: String){
        imageNameObservable.value = imageName
    }

    fun deleteFile(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val file = readWriteUtils.createImageFile(name)
            if (file?.exists() == true) {
                file.delete()
            }
        }
    }

    suspend fun renameFile(uri: Uri?, newName: String): String? {
        return withContext(Dispatchers.IO) {
            val oldFile = File(uri?.path)
            val newFile = readWriteUtils.createImageFile(newName)
            Timber.d("cretino  ${oldFile.path} ${newFile?.path}")
            val done = oldFile.renameTo(newFile)
            if(done) {
                newFile?.name
            }else{
                null
            }
        }
    }

}