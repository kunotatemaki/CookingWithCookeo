package com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.rukiasoft.androidapps.cocinaconroll.R
import com.rukiasoft.androidapps.cocinaconroll.databinding.FragmentStep1Binding
import javax.inject.Inject


class Step1Fragment : ChildBaseFragment() {

    private lateinit var binding: FragmentStep1Binding

    @Inject
    lateinit var appContext: Context

    override val childPosition: NewRecipeParent.ChildPosition
        get() = NewRecipeParent.ChildPosition.FIRST


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_step1, container, false, cookeoBindingComponent)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val list = listOf(
            resourcesManager.getString(R.string.starters),
            resourcesManager.getString(R.string.main_courses),
            resourcesManager.getString(R.string.desserts)
        )

        val dataAdapter = ArrayAdapter(appContext, android.R.layout.simple_spinner_item, list)

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerTypeDish.adapter = dataAdapter
    }

}
