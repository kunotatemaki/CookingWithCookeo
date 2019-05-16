package com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation

import android.content.Context
import com.rukiasoft.androidapps.cocinaconroll.ui.common.BaseFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi


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

abstract class ChildBaseFragment : BaseFragment() {

    protected lateinit var listener: NewRecipeParent

    abstract val childPosition: NewRecipeParent.ChildPosition

    override fun onResume() {
        super.onResume()
        listener.setFragmentSelected(childPosition)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (parentFragment?.parentFragment is NewRecipeParent) {
            listener = parentFragment?.parentFragment as NewRecipeParent
        } else {
            throw IllegalArgumentException()
        }
    }

    abstract fun validateFields(): Boolean

}
