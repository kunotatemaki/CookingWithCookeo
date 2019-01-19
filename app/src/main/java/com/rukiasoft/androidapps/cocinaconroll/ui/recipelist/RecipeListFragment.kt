package com.rukiasoft.androidapps.cocinaconroll.ui.recipelist

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.*
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rukiasoft.androidapps.cocinaconroll.R
import com.rukiasoft.androidapps.cocinaconroll.databinding.CookeoBindingComponent
import com.rukiasoft.androidapps.cocinaconroll.databinding.FragmentRecipeListBinding
import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Recipe
import com.rukiasoft.androidapps.cocinaconroll.ui.common.BaseFragment
import com.rukiasoft.androidapps.cocinaconroll.ui.common.MainActivity
import com.rukiasoft.androidapps.cocinaconroll.ui.common.MainViewModel


class RecipeListFragment : BaseFragment(), RecipeListAdapter.OnRecipeClicked {


    private lateinit var viewModel: RecipeListViewModel

    private lateinit var binding: FragmentRecipeListBinding

    private lateinit var adapter: RecipeListAdapter

    private var searchMenuItem: MenuItem? = null
    private var mOpenCircleRevealX: Int = 0
    private var mOpenCircleRevealY: Int = 0
    private var mMagnifyingX: Int = 0
    private var mMagnifyingY: Int = 0
    private var animate: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
            CookeoBindingComponent()
        )

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(RecipeListViewModel::class.java)

        adapter = RecipeListAdapter(this)
        binding.recipeList.adapter = adapter
        binding.recipeList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        (activity as? MainActivity)?.setToolbar(binding.toolbarRecipeListFragment, true)

        (activity as? MainActivity)?.getMainViewModel()?.let { mainViewModel ->
            mainViewModel.getListOfRecipes().observe(this, Observer { it ->
                it?.let {
                    submitListToAdapter(it)
                }
            })

            mainViewModel.getFilterAsObservable().observe(this, Observer { it ->
                it?.let {
                    setIcon(it.first)
                }
            })
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_recipe_list, menu)
        menu?.findItem(R.id.action_search)?.let {

            searchMenuItem = it
            it.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {

                override fun onMenuItemActionCollapse(item: MenuItem): Boolean {


                    val colorFrom = resourcesManager.getColor(R.color.colorPrimarySearch)
                    val colorTo = resourcesManager.getColor(R.color.colorPrimary)
                    val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
                    colorAnimation.duration = 250 // milliseconds
                    colorAnimation.addUpdateListener { animator ->
                        binding.toolbarRecipeListFragment.setBackgroundColor(animator.animatedValue as Int)
                    }
                    colorAnimation.start()

                    //show the bar and button
                    setVisibilityWithSearchWidget(View.VISIBLE)
                    (activity as? MainActivity)?.getMainViewModel()?.setFilter(MainViewModel.FilterType.ALL)
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
                    binding.toolbarRecipeListFragment.setBackgroundResource(R.color.colorPrimarySearch)
                    //hide the bar and button
                    setVisibilityWithSearchWidget(View.GONE)
                    //hide the floating button

                    return true
                }

            })

            (searchMenuItem?.actionView as? SearchView)?.setOnQueryTextListener(object :
                SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean = true

                override fun onQueryTextChange(p0: String?): Boolean {
//                    if (p0?.length ?: 0 > 1) {
                        (activity as? MainActivity)?.getMainViewModel()?.setFilter(MainViewModel.FilterType.BY_NAME, p0)
//                    }
                    return true
                }

            })

//            val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
//            //the searchable is in another activity, so instead of getcomponentname(), create a new one for that activity
//            searchView.setSearchableInfo(
//                searchManager.getSearchableInfo(
//                    ComponentName(
//                        this,
//                        SearchableActivity::class.java
//                    )
//                )
//            )
        }

    }

    fun setVisibilityWithSearchWidget(visibility: Int) {
        binding.numberandtypeRecipesBar.visibility = visibility
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

    override fun recipeSelected(recipeKey: String?) {
        recipeKey?.let {
            findNavController().navigate(
                RecipeListFragmentDirections.actionRecipeListFragmentToRecipeDetailsFragment(
                    it
                )
            )
        }
    }
}
