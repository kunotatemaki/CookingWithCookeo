package com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rukiasoft.androidapps.cocinaconroll.R
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class Step3Fragment : ChildBaseFragment() {

    override val childPosition: NewRecipeParent.ChildPosition
        get() = NewRecipeParent.ChildPosition.THIRD


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_step3, container, false)
    }

    override fun validateFields(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
