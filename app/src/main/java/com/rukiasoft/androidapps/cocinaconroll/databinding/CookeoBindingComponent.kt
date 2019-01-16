package com.rukiasoft.androidapps.cocinaconroll.databinding

import androidx.databinding.DataBindingComponent
import com.rukiasoft.androidapps.cocinaconroll.extensions.CookeoBindingAdapters

class CookeoBindingComponent : DataBindingComponent {
    override fun getCookeoBindingAdapters(): CookeoBindingAdapters {
        return CookeoBindingAdapters()
    }
}