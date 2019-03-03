package com.rukiasoft.androidapps.cocinaconroll.ui.common

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.google.android.material.navigation.NavigationView
import com.rukiasoft.androidapps.cocinaconroll.NavGraphDirections
import com.rukiasoft.androidapps.cocinaconroll.R
import com.rukiasoft.androidapps.cocinaconroll.databinding.ActivityMainBinding
import com.rukiasoft.androidapps.cocinaconroll.viewmodel.CocinaConRollViewModelFactory
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    @Inject
    lateinit var context: Context

    @Inject
    lateinit var viewModelFactory: CocinaConRollViewModelFactory

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)

        downloadRecipesFromFirebase()

        viewModel.downloadingState().observe(this, Observer {
            it?.let { loading ->
                //todo poner el loading
            }
        })
        if (viewModel.isFirstLoading()) {
            viewModel.setAppLoaded()
            findNavController(R.id.fragment_container).navigate(
                NavGraphDirections.actionGlobalSignInFragment()
            )
        }
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
//                MainViewModel.setFilter(MainViewModel.FilterType.OWN)
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
                val toggle = ActionBarDrawerToggle(
                    this@MainActivity, binding.drawerLayout, toolbar,
                    R.string.navigation_drawer_open,
                    R.string.navigation_drawer_close
                )
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

}
