package com.rukiasoft.androidapps.cocinaconroll.ui.common


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.rukiasoft.androidapps.cocinaconroll.R
import com.rukiasoft.androidapps.cocinaconroll.resources.ResourcesManager
import com.rukiasoft.androidapps.cocinaconroll.viewmodel.CocinaConRollViewModelFactory
import dagger.android.support.DaggerFragment
import javax.inject.Inject



open class BaseFragment : DaggerFragment() {

    @Inject
    protected lateinit var resourcesManager: ResourcesManager

    @Inject
    protected lateinit var viewModelFactory: CocinaConRollViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return TextView(activity).apply {
            setText(R.string.hello_blank_fragment)
        }
    }


}
