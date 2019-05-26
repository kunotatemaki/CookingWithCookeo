package com.rukiasoft.androidapps.cocinaconroll.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import java.lang.ref.WeakReference
import java.text.Normalizer

fun String.normalizedString(): String {
    val normalized: String = Normalizer.normalize(this, Normalizer.Form.NFD)
    return normalized.replace("[^\\p{ASCII}]".toRegex(), "").trim { it <= ' ' }.toLowerCase()
}

fun <X, Y> LiveData<X>.switchMap(func: (X) -> LiveData<Y>)
        = Transformations.switchMap(this, func)

fun <T> WeakReference<T>.safe(body : T.() -> Unit) {
    this.get()?.body()
}

fun <T> LiveData<T>.getDistinct(): LiveData<T> {
    val distinctLiveData = MediatorLiveData<T>()
    distinctLiveData.addSource(this, object : Observer<T> {
        private var initialized = false
        private var lastObj: T? = null
        val test = 4.toDouble()
        override fun onChanged(obj: T?) {
            if (!initialized) {
                initialized = true
                lastObj = obj
                distinctLiveData.postValue(lastObj)
            } else if ((obj == null && lastObj != null)
                || obj != lastObj) {
                lastObj = obj
                distinctLiveData.postValue(lastObj)
            }
        }
    })
    return distinctLiveData
}