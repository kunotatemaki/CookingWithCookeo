package com.rukiasoft.androidapps.cocinaconroll.ui.common


import android.os.Bundle
import com.rukiasoft.androidapps.cocinaconroll.CocinaConRollApplication
import com.rukiasoft.androidapps.cocinaconroll.databinding.CookeoBindingComponent
import com.rukiasoft.androidapps.cocinaconroll.firebase.FirebaseUtils
import com.rukiasoft.androidapps.cocinaconroll.permission.PermissionManager
import com.rukiasoft.androidapps.cocinaconroll.persistence.PersistenceManager
import com.rukiasoft.androidapps.cocinaconroll.preferences.PreferencesManager
import com.rukiasoft.androidapps.cocinaconroll.resources.ResourcesManager
import com.rukiasoft.androidapps.cocinaconroll.utils.*
import com.rukiasoft.androidapps.cocinaconroll.viewmodel.CocinaConRollViewModelFactory
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import javax.inject.Inject


@ExperimentalCoroutinesApi
open class BaseFragment : DaggerFragment(), CoroutineScope by MainScope() {

    @Inject
    protected lateinit var resourcesManager: ResourcesManager

    @Inject
    protected lateinit var preferenceManager: PreferencesManager

    @Inject
    protected lateinit var viewModelFactory: CocinaConRollViewModelFactory

    @Inject
    protected lateinit var generalUtils: GeneralUtils

    @Inject
    protected lateinit var cookeoBindingComponent: CookeoBindingComponent

    @Inject
    protected lateinit var persistenceManager: PersistenceManager

    @Inject
    protected lateinit var deviceUtils: DeviceUtils

    @Inject
    protected lateinit var cocinaConRollApplication: CocinaConRollApplication

    @Inject
    protected lateinit var viewUtils: ViewUtils

    @Inject
    protected lateinit var firebaseUtils: FirebaseUtils

    @Inject
    protected lateinit var permissionManager: PermissionManager

    @Inject
    protected lateinit var readWriteUtils: ReadWriteUtils

    @Inject
    protected lateinit var mediaUtils: MediaUtils

    @Inject
    protected lateinit var fileProviderUtils: FileProviderUtils

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as? MainActivity)?.refreshAd()
    }

    override fun onDestroy() {
        cancel()
        super.onDestroy()

    }

}
