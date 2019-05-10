package com.rukiasoft.androidapps.cocinaconroll.ui.common

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.Constraints
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.material.navigation.NavigationView
import com.rukiasoft.androidapps.cocinaconroll.BuildConfig
import com.rukiasoft.androidapps.cocinaconroll.NavGraphDirections
import com.rukiasoft.androidapps.cocinaconroll.R
import com.rukiasoft.androidapps.cocinaconroll.databinding.ActivityMainBinding
import com.rukiasoft.androidapps.cocinaconroll.resources.ResourcesManager
import com.rukiasoft.androidapps.cocinaconroll.ui.signin.SignInViewModel
import com.rukiasoft.androidapps.cocinaconroll.ui.views.LoadingView
import com.rukiasoft.androidapps.cocinaconroll.utils.ViewUtils
import com.rukiasoft.androidapps.cocinaconroll.viewmodel.CocinaConRollViewModelFactory
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.ref.WeakReference
import javax.inject.Inject


class MainActivity : DaggerAppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    GoogleApiClient.OnConnectionFailedListener {

    @Inject
    lateinit var context: Context

    @Inject
    lateinit var viewModelFactory: CocinaConRollViewModelFactory

    @Inject
    lateinit var viewUtils: ViewUtils

    @Inject
    lateinit var resourcesManager: ResourcesManager

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: MainViewModel
    private lateinit var signInViewModel: SignInViewModel

    private var loadingView: LoadingView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        signInViewModel = ViewModelProviders.of(this, viewModelFactory).get(SignInViewModel::class.java)
        signInViewModel.initializeConnection(this, this)

        if(savedInstanceState == null) {
            //avoid check on configuration changes
            downloadRecipesFromFirebase()
        }

        viewModel.downloadingState().observe(this, Observer {
            it?.let { state ->
                if (state > 0) {
                    showLoading()
                } else {
                    hideLoading()
                }
            }
        })
    }

    fun refreshAd(){
        val adRequest = getAd()
        binding.adView.loadAd(adRequest)
    }

    fun getAd(): AdRequest =AdRequest.Builder()
            .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
            .addTestDevice(BuildConfig.PIXEL_2)  //todo get code for My Pixel 2 test device
            .addTestDevice(BuildConfig.Z3_DEVICE_ID)  //todo get code for My Pixel 2 test device
            .build()


    fun downloadRecipesFromFirebase() = viewModel.downloadRecipesFromFirebase()

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            if (findNavController(R.id.fragment_container).currentDestination?.id == R.id.recipe_list_fragment) {
                viewUtils.showAlertDialog(
                    activity = WeakReference(this),
                    allowCancelWhenTouchingOutside = false,
                    title = resourcesManager.getString(R.string.exit_application_title),
                    message = String.format(resourcesManager.getString(R.string.exit_application), resourcesManager.getString(applicationInfo.labelRes)),
                    positiveButton = resourcesManager.getString(R.string.accept),
                    callbackPositive = {finish()},
                    negativeButton = resourcesManager.getString(R.string.cancel)
                )
            } else {
                super.onBackPressed()
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_all_recipes -> {
                viewModel.setFilter(MainViewModel.FilterType.ALL)
            }
            R.id.menu_starters -> {
                viewModel.setFilter(MainViewModel.FilterType.STARTER)
            }
            R.id.menu_main_courses -> {
                viewModel.setFilter(MainViewModel.FilterType.MAIN)
            }
            R.id.menu_desserts -> {
                viewModel.setFilter(MainViewModel.FilterType.DESSERT)
            }
            R.id.menu_vegetarians -> {
                viewModel.setFilter(MainViewModel.FilterType.VEGETARIAN)
            }
            R.id.menu_favorites -> {
                viewModel.setFilter(MainViewModel.FilterType.FAVOURITE)
            }
            R.id.menu_own_recipes -> {
                viewModel.setFilter(MainViewModel.FilterType.OWN)
            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun getMainViewModel(): MainViewModel = viewModel

    fun setToolbar(toolbar: Toolbar, showHamburger: Boolean, title: String? = null) {
        setSupportActionBar(null)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(true)
            if (showHamburger) {

                val toggle = object : ActionBarDrawerToggle(
                    this@MainActivity, binding.drawerLayout, toolbar,
                    R.string.navigation_drawer_open,
                    R.string.navigation_drawer_close
                ) {

                    override fun onDrawerOpened(drawerView: View) {

                        binding.navView.menu.findItem(R.id.menu_own_recipes)?.isVisible = viewModel.getOwnRecipes()

                        super.onDrawerOpened(drawerView)

                    }
                }

                binding.drawerLayout.addDrawerListener(toggle)
                toggle.syncState()
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                nav_view.setNavigationItemSelectedListener(this@MainActivity)
            } else {
                nav_view.setNavigationItemSelectedListener(null)
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }
            this.title = title
        }
    }

    fun updateStatusBar(color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = color
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val setStatusBarThemeAsDark = viewUtils.needToSetStatusBarThemeAsDark(color)
            setSystemBarTheme(setStatusBarThemeAsDark)
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun setSystemBarTheme(darkBackgroundTheme: Boolean) {
        val lFlags = this.window.decorView.systemUiVisibility
        this.window.decorView.systemUiVisibility =
            if (darkBackgroundTheme) lFlags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv() else lFlags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun showLoading() {
        if (loadingView != null) {
            return
        }
        createLoadingView().apply {
            loadingView = this
            show()
        }
    }

    private fun createLoadingView(): LoadingView = LoadingView(this, null).apply {

        val layoutParams =
            Constraints.LayoutParams(Constraints.LayoutParams.MATCH_PARENT, Constraints.LayoutParams.MATCH_PARENT)

        this.layoutParams = layoutParams

        getAvailableViewForShowingLoading()?.addView(this)
    }

    fun hideLoading() {
        val contentView = getAvailableViewForShowingLoading()
        loadingView?.dismiss()
        contentView?.removeViewInLayout(loadingView)
        loadingView = null
        if (viewModel.isFirstLoading()) {
            viewModel.setAppLoaded()
            findNavController(R.id.fragment_container).navigate(
                NavGraphDirections.actionGlobalSignInFragment()
            )
        }
    }

    private fun getAvailableViewForShowingLoading(): ViewGroup? {
        val contentView = findViewById<ViewGroup>(android.R.id.content)
        if (contentView?.childCount ?: 0 > 0) {
            val rootView = contentView?.getChildAt(0)
            if (rootView is DrawerLayout && (rootView as ViewGroup).childCount > 0) {
                for (i in 0 until rootView.childCount) {
                    if (rootView.getChildAt(i) !is NavigationView) {
                        return rootView.getChildAt(i) as ViewGroup?
                    }
                }
            } else {
                return rootView as ViewGroup?
            }
        }
        return null
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        hideLoading()
    }

    fun getSigningVM() = signInViewModel

    fun clickOnSelectedType() {
        val menu = binding.navView.menu
        for (i in 0 until menu.size()) {
            val item = menu.getItem(i)
            if (item.isChecked) {
                onNavigationItemSelected(item)
                return
            }
        }
    }

    fun keepScreenOn(screenOn: Boolean) {
        if (screenOn) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    fun hideKeyboard() {
        this.currentFocus?.let { view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.applicationWindowToken, 0)
        }
    }

}
