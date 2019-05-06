package com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.rukiasoft.androidapps.cocinaconroll.R
import java.lang.IllegalArgumentException


class Step1Fragment : ChildBaseFragment() {

    override val childPosition: NewRecipeParent.ChildPosition
        get() = NewRecipeParent.ChildPosition.FIRST

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_step1, container, false)
    }



}
