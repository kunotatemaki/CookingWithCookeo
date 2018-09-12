package com.rukiasoft.androidapps.cocinaconroll

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel;
import javax.inject.Inject

class BlankViewModel @Inject constructor(private val context: Context): ViewModel() {
    // TODO: Implement the ViewModel

    fun test(){
        Log.d("", "")
    }
}
