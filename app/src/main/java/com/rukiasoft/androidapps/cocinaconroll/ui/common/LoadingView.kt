package com.rukiasoft.androidapps.cocinaconroll.ui.common

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.rukiasoft.androidapps.cocinaconroll.R


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

    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.loading_view, this, true)
    }
}