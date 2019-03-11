package com.rukiasoft.androidapps.cocinaconroll.ui.common

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.Constraints
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.material.navigation.NavigationView
import com.rukiasoft.androidapps.cocinaconroll.NavGraphDirections
import com.rukiasoft.androidapps.cocinaconroll.R
import com.rukiasoft.androidapps.cocinaconroll.databinding.ActivityMainBinding
import com.rukiasoft.androidapps.cocinaconroll.ui.signin.SignInViewModel
import com.rukiasoft.androidapps.cocinaconroll.viewmodel.CocinaConRollViewModelFactory
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : DaggerAppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    @Inject
    lateinit var context: Context

    @Inject
    lateinit var viewModelFactory: CocinaConRollViewModelFactory

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

        downloadRecipesFromFirebase()

        viewModel.downloadingState().observe(this, Observer {
            it?.let { state ->
                if(state > 0){
                    showLoading()
                }else{
                    hideLoading()
                }
            }
        })

    }

    fun downloadRecipesFromFirebase() = viewModel.downloadRecipesFromFirebase()

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
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
            val setStatusBarThemeAsDark = needToSetStatusBarThemeAsDark(color)
            setSystemBarTheme(setStatusBarThemeAsDark)
        }

    }

    private fun needToSetStatusBarThemeAsDark(color: Int): Boolean {

        var red = Color.red(color) / 255.0
        red = if (red < 0.03928) red / 12.92 else Math.pow((red + 0.055) / 1.055, 2.4)
        var green = Color.green(color) / 255.0
        green = if (green < 0.03928) green / 12.92 else Math.pow((green + 0.055) / 1.055, 2.4)
        var blue = Color.blue(color) / 255.0
        blue = if (blue < 0.03928) blue / 12.92 else Math.pow((blue + 0.055) / 1.055, 2.4)
        return (0.2126 * red + 0.7152 * green + 0.0722 * blue).toFloat() < 0.5
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
//        loadingView?.dismiss()
        if(loadingView!= null){
            return
        }
        createLoadingView().apply {
            loadingView = this
//            show(bookmakersRepository, appExecutors)
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

}
