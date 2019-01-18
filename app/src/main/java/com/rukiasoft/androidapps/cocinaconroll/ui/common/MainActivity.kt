package com.rukiasoft.androidapps.cocinaconroll.ui.common

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.navigation.NavigationView
import com.rukiasoft.androidapps.cocinaconroll.R
import com.rukiasoft.androidapps.cocinaconroll.databinding.ActivityMainBinding
import com.rukiasoft.androidapps.cocinaconroll.viewmodel.CocinaConRollViewModelFactory
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
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

        viewModel.downloadRecipesFromFirebase()
//        setSupportActionBar(toolbar)
//
//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
//        }
//
//        Debug.waitForDebugger()


        viewModel.downloadingState().observe(this, Observer { it ->
            it?.let {
                Timber.d("cretino $it")
            }
        })
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
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

    fun setToolbar(toolbar: Toolbar, showHamburger: Boolean) {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(true)
            if (showHamburger) {
                setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
            }
            val toggle = ActionBarDrawerToggle(
                this@MainActivity, binding.drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
            )
            drawer_layout.addDrawerListener(toggle)
            toggle.syncState()

            nav_view.setNavigationItemSelectedListener(this@MainActivity)
        }
    }

}
