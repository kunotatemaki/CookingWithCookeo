package com.rukiasoft.androidapps.cocinaconroll.ui.common


import com.rukiasoft.androidapps.cocinaconroll.preferences.PreferencesManager
import com.rukiasoft.androidapps.cocinaconroll.resources.ResourcesManager
import com.rukiasoft.androidapps.cocinaconroll.viewmodel.CocinaConRollViewModelFactory
import dagger.android.support.DaggerFragment
import javax.inject.Inject


open class BaseFragment : DaggerFragment() {

    @Inject
    protected lateinit var resourcesManager: ResourcesManager

    @Inject
    protected lateinit var preferenceManager: PreferencesManager

    @Inject
    protected lateinit var viewModelFactory: CocinaConRollViewModelFactory


}
