package com.rukiasoft.androidapps.cocinaconroll.ui.recipelist

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavDirections
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.InterstitialAd
import com.rukiasoft.androidapps.cocinaconroll.NavGraphDirections
import com.rukiasoft.androidapps.cocinaconroll.R
import com.rukiasoft.androidapps.cocinaconroll.databinding.RecipeListFragmentBinding
import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Recipe
import com.rukiasoft.androidapps.cocinaconroll.preferences.PreferencesConstants
import com.rukiasoft.androidapps.cocinaconroll.ui.common.BaseFragment
import com.rukiasoft.androidapps.cocinaconroll.ui.common.MainActivity
import com.rukiasoft.androidapps.cocinaconroll.ui.common.MainViewModel
import com.rukiasoft.androidapps.cocinaconroll.ui.signin.SignInViewModel
import com.rukiasoft.androidapps.cocinaconroll.utils.GeneralConstants
import java.lang.ref.WeakReference


class RecipeListFragment : BaseFragment(), RecipeListAdapter.OnRecipeClicked {


    private lateinit var viewModel: RecipeListViewModel
    private var signingViewModel: SignInViewModel? = null

    private lateinit var binding: RecipeListFragmentBinding

    private lateinit var adapter: RecipeListAdapter

    private var searchMenuItem: MenuItem? = null
    private var mOpenCircleRevealX: Int = 0
    private var mOpenCircleRevealY: Int = 0
    private var mMagnifyingX: Int = 0
    private var mMagnifyingY: Int = 0
    private var animate: Boolean = false
    private lateinit var interstitialAd: InterstitialAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        interstitialAd = InterstitialAd(activity).apply {
            adUnitId = resources.getString(R.string.banner_ad_unit_id_intersticial)
            adListener = object : AdListener() {
                override fun onAdClosed() {
                    requestNewInterstitial()
                    if (viewModel.directions != null && viewModel.extras != null) {
                        navigate(viewModel.directions, viewModel.extras)
                        viewModel.directions = null
                        viewModel.extras = null
                    }
                }
            }
        }
        requestNewInterstitial()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.recipe_list_fragment,
            container,
            false,
            cookeoBindingComponent
        )

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(RecipeListViewModel::class.java)
        signingViewModel = (activity as? MainActivity)?.getSigningVM()

        adapter = RecipeListAdapter(
            listener = this,
            cookeoBindingComponent = cookeoBindingComponent,
            resourcesManager = resourcesManager,
            persistenceManager = persistenceManager
        )
        binding.recipeList.adapter = adapter
        binding.recipeList.setHasFixedSize(true)
        val columnCount = resourcesManager.getInteger(R.integer.list_column_count)
        binding.recipeList.layoutManager = GridLayoutManager(context, columnCount)
        binding.recipeList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0)
                    binding.addRecipeFab.hide()
                else if (dy < 0)
                    binding.addRecipeFab.show()
            }
        })

        binding.addRecipeFab.setOnClickListener {
            if (preferenceManager.containsKey(PreferencesConstants.PROPERTY_FIREBASE_ID)) {
                findNavController().navigate(
                    NavGraphDirections.actionGlobalNewRecipeContainerFragment(null)
                )
            } else {
                activity?.let {
                    viewUtils.showAlertDialog(
                        activity = WeakReference(it),
                        allowCancelWhenTouchingOutside = false,
                        title = resourcesManager.getString(R.string.permissions_title),
                        message = resourcesManager.getString(R.string.create_recipe_explanation),
                        positiveButton = resourcesManager.getString(R.string.sign_in),
                        callbackPositive = { findNavController().navigate(NavGraphDirections.actionGlobalSignInFragment()) },
                        negativeButton = resourcesManager.getString(R.string.cancel)
                    )
                }
            }
        }

        (activity as? MainActivity)?.apply {
            setToolbar(
                binding.toolbarRecipeListFragment,
                true,
                resourcesManager.getString(R.string.app_name)
            )
            updateStatusBar(resourcesManager.getColor(android.R.color.white))
        }

        (activity as? MainActivity)?.getMainViewModel()?.let { mainViewModel ->
            mainViewModel.getListOfRecipes().observe(this, Observer {
                it?.let { list ->
                    submitListToAdapter(list)
                }
            })

            mainViewModel.getFilterAsObservable().observe(this, Observer {
                it?.let { list ->
                    setIcon(list.first)
                }
            })
        }

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_recipe_list, menu)
        menu.findItem(R.id.action_search)?.also {

            searchMenuItem = it
            it.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {

                override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                    val backgroundColorFrom = resourcesManager.getColor(R.color.colorPrimarySearch)
                    val backgroundColorTo = resourcesManager.getColor(R.color.colorPrimary)
                    val backgroundColorAnimation =
                        ValueAnimator.ofObject(ArgbEvaluator(), backgroundColorFrom, backgroundColorTo)
                    backgroundColorAnimation.duration = 300 // milliseconds
                    val toolbarBackground = binding.toolbarRecipeListFragment.background as GradientDrawable
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && toolbarBackground.color == null)
                        return true
                    backgroundColorAnimation.addUpdateListener { animator ->
                        val color = animator.animatedValue as Int
                        toolbarBackground.setColor(color)
                    }
                    val strokeColorFrom = resourcesManager.getColor(R.color.toolbar_border_search)
                    val strokeColorTo = resourcesManager.getColor(R.color.toolbar_border)
                    val strokeColorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), strokeColorFrom, strokeColorTo)
                    strokeColorAnimation.duration = 300 // milliseconds
                    val strokeSize = deviceUtils.getPxFromDp(1f).toInt()
                    strokeColorAnimation.addUpdateListener { animator ->
                        val color = animator.animatedValue as Int
                        toolbarBackground.setStroke(strokeSize, color)
                    }
                    backgroundColorAnimation.start()
                    strokeColorAnimation.start()
                    //show the bar and button
                    setVisibilityWithSearchWidget(View.VISIBLE)
                    (activity as? MainActivity)?.clickOnSelectedType()
                    return true
                }

                override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                    animate = true
                    binding.toolbarRecipeListFragment.addOnLayoutChangeListener(object :
                        View.OnLayoutChangeListener {
                        override fun onLayoutChange(
                            v: View,
                            left: Int,
                            top: Int,
                            right: Int,
                            bottom: Int,
                            oldLeft: Int,
                            oldTop: Int,
                            oldRight: Int,
                            oldBottom: Int
                        ) {
                            v.removeOnLayoutChangeListener(this)
                            val animator = ViewAnimationUtils.createCircularReveal(
                                binding.toolbarRecipeListFragment,
                                mMagnifyingX,
                                mMagnifyingY,
                                0f,
                                Math.hypot(
                                    binding.toolbarRecipeListFragment.width.toDouble(),
                                    binding.toolbarRecipeListFragment.height.toDouble()
                                ).toFloat()
                            )
                            mOpenCircleRevealX = mMagnifyingX
                            mOpenCircleRevealY = mMagnifyingY
                            // Set a natural ease-in/ease-out interpolator.
                            animator.interpolator = AccelerateDecelerateInterpolator()

                            // make the view visible and start the animation
                            animator.start()
                        }
                    })
                    val searchColor = resourcesManager.getColor(R.color.colorPrimarySearch)
                    val toolbarBackground = binding.toolbarRecipeListFragment.background as GradientDrawable
                    toolbarBackground.setColor(searchColor)

                    val searchStrokeColor = resourcesManager.getColor(R.color.toolbar_border_search)
                    val strokeSize = deviceUtils.getPxFromDp(1f).toInt()
                    toolbarBackground.setStroke(strokeSize, searchStrokeColor)

                    setVisibilityWithSearchWidget(View.GONE)

                    return true
                }

            })

            (searchMenuItem?.actionView as? SearchView)?.setOnQueryTextListener(object :
                SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean = true

                override fun onQueryTextChange(p0: String?): Boolean {
                    (activity as? MainActivity)?.getMainViewModel()?.setFilter(MainViewModel.FilterType.BY_NAME, p0)
                    return true
                }

            })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_thanks -> {
                findNavController().navigate(NavGraphDirections.actionGlobalThanksFragment())
                true
            }
            R.id.menu_sign_in -> {
                findNavController().navigate(NavGraphDirections.actionGlobalSignInFragment())
                true
            }
            R.id.menu_sign_out -> {
                signingViewModel?.revokeAccess()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        if (preferenceManager.containsKey(PreferencesConstants.PROPERTY_FIREBASE_ID)) {
            menu.findItem(R.id.menu_sign_out).isVisible = true
            menu.findItem(R.id.menu_sign_in).isVisible = false
        } else {
            menu.findItem(R.id.menu_sign_out).isVisible = false
            menu.findItem(R.id.menu_sign_in).isVisible = true
        }
    }

    fun setVisibilityWithSearchWidget(visibility: Int) {
        binding.numberAndTypeRecipesBar.visibility = visibility
        if (visibility == View.GONE)
            binding.addRecipeFab.hide()
        else
            binding.addRecipeFab.show()
    }

    private fun submitListToAdapter(list: PagedList<Recipe>) {
        adapter.submitList(list)
        binding.recipeListNumberRecipes.text =
            String.format(resourcesManager.getString(R.string.recipes), list.size)
    }

    private fun setIcon(filter: MainViewModel.FilterType) {
        val icon: Int?
        val text: String?
        when (filter) {
            MainViewModel.FilterType.ALL -> {
                icon = R.drawable.ic_all_24
                text = resourcesManager.getString(R.string.all_recipes)
            }
            MainViewModel.FilterType.STARTER -> {
                icon = R.drawable.ic_starters_24
                text = resourcesManager.getString(R.string.starters)
            }
            MainViewModel.FilterType.MAIN -> {
                icon = R.drawable.ic_main_24
                text = resourcesManager.getString(R.string.main_courses)
            }
            MainViewModel.FilterType.DESSERT -> {
                icon = R.drawable.ic_dessert_24
                text = resourcesManager.getString(R.string.desserts)
            }
            MainViewModel.FilterType.VEGETARIAN -> {
                icon = R.drawable.ic_vegetarians_24
                text = resourcesManager.getString(R.string.vegetarians)
            }
            MainViewModel.FilterType.FAVOURITE -> {
                icon = R.drawable.ic_favorite_black_24dp
                text = resourcesManager.getString(R.string.favourites)
            }
            MainViewModel.FilterType.OWN -> {
                icon = R.drawable.ic_own_24
                text = resourcesManager.getString(R.string.own_recipes)
            }
            else -> {
                icon = null
                text = null
            }
        }
        icon?.let { binding.iconId = icon }
        text?.let { binding.recipeListTypeRecipes.text = text }
        binding.executePendingBindings()
    }

    override fun onResume() {
        super.onResume()
        closeSearchView()
        //to start the reveal effect on the magnifying glass
        val viewTreeObserver = activity?.window?.decorView?.viewTreeObserver
        viewTreeObserver?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                activity?.findViewById<View>(R.id.action_search)?.let { menuButton ->
                    // This could be called when the button is not there yet, so we must test for null

                    // Found it! Do what you need with the button
                    val location = IntArray(2)
                    menuButton.getLocationInWindow(location)
                    //Log.d(TAG, "x=" + location[0] + " y=" + location[1]);
                    mMagnifyingX = location[0] + menuButton.width / 2
                    mMagnifyingY = location[1]
                    // Now you can get rid of this listener
                    if (mMagnifyingX != 0 && mMagnifyingY != 0 && viewTreeObserver.isAlive) {
                        viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                }
            }
        })
    }

    private fun closeSearchView() {
        animate = false
        searchMenuItem?.collapseActionView()
    }

    override fun recipeSelected(recipe: Recipe?, view: View) {
        recipe?.let {
            val transitionName = ViewCompat.getTransitionName(view) ?: ""
            val extras = FragmentNavigatorExtras(
                view to transitionName
            )
            viewModel.extras = extras
            viewModel.directions = NavGraphDirections.actionGlobalRecipeDetailsFragment(
                it.recipeKey, transitionName,
                it.colorClear ?: resourcesManager.getColor(R.color.colorPrimaryRed),
                it.colorDark ?: resourcesManager.getColor(android.R.color.white)
            )
            var number = preferenceManager.getIntFromPreferences(PreferencesConstants.PREFERENCE_INTERSTITIAL)
            if ((number in 0..GeneralConstants.N_RECIPES_TO_INTERSTICIAL).not()) {
                number = 0
            }

            if (number != GeneralConstants.N_RECIPES_TO_INTERSTICIAL || interstitialAd.isLoaded.not()) {
                navigate(viewModel.directions, viewModel.extras)
            } else {
                interstitialAd.show()
                number = 0
            }
            preferenceManager.setIntIntoPreferences(PreferencesConstants.PREFERENCE_INTERSTITIAL, ++number)

        }
    }

    private fun navigate(directions: NavDirections?, extras: Navigator.Extras?) {
        directions?.let {
            extras?.let {
                findNavController().navigate(directions, extras)
            }
        }
    }

    private fun requestNewInterstitial() {
        if (interstitialAd.isLoaded.not()) {
            (activity as? MainActivity)?.getAd()
            val adRequest = (activity as? MainActivity)?.getAd()
            interstitialAd.loadAd(adRequest)
        }
    }

    override fun updateCard(position: Int) {
        adapter.notifyItemChanged(position)
    }
}
