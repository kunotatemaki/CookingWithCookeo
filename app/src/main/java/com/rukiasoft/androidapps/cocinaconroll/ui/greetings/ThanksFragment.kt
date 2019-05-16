package com.rukiasoft.androidapps.cocinaconroll.ui.greetings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.rukiasoft.androidapps.cocinaconroll.R
import com.rukiasoft.androidapps.cocinaconroll.databinding.ThanksFragmentBinding
import com.rukiasoft.androidapps.cocinaconroll.ui.common.BaseFragment
import com.rukiasoft.androidapps.cocinaconroll.ui.common.MainActivity
import com.rukiasoft.androidapps.cocinaconroll.utils.GeneralConstants
import kotlinx.coroutines.ExperimentalCoroutinesApi


/**
 * Copyright (C) Rukiasoft - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Roll <raulfeliz@gmail.com>, January 2019
 *
 *
 */

class ThanksFragment : BaseFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    private lateinit var binding: ThanksFragmentBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)

        binding =
                DataBindingUtil.inflate(inflater, R.layout.thanks_fragment, container, false, cookeoBindingComponent)
        val support = String.format(
            resourcesManager.getString(R.string.support_recipes),
            generalUtils.getApplicationName(), GeneralConstants.EMAIL
        )
        binding.textViewSupportRecipes.text = support

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as? MainActivity)?.setToolbar(
            binding.thanksToolbar.standardToolbar,
            false,
            resourcesManager.getString(R.string.thanks)
        )

    }

}
