package com.rukiasoft.androidapps.cocinaconroll.databinding

import androidx.databinding.DataBindingComponent
import javax.inject.Inject

class CookeoBindingComponent @Inject constructor(private val cookeoBindingAdapters: CookeoBindingAdapters) : DataBindingComponent {
    override fun getCookeoBindingAdapters(): CookeoBindingAdapters {
        return cookeoBindingAdapters
    }
}