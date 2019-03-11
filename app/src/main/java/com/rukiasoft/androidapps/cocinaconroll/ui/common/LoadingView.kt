package com.rukiasoft.androidapps.cocinaconroll.ui.common

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.rukiasoft.androidapps.cocinaconroll.R
import com.rukiasoft.androidapps.cocinaconroll.databinding.LoadingViewBinding


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

class LoadingView(context: Context, attributeSet: AttributeSet?) : ConstraintLayout(context, attributeSet) {

    private var binding: LoadingViewBinding
    init {
        val inflater = LayoutInflater.from(context)
        binding = DataBindingUtil.inflate(inflater, R.layout.loading_view, this, true)
    }

    fun show(){
        (binding.loadingAnimation.drawable as? AnimationDrawable)?.start()
    }
    fun dismiss(){
        (binding.loadingAnimation.drawable as? AnimationDrawable)?.stop()
    }
}