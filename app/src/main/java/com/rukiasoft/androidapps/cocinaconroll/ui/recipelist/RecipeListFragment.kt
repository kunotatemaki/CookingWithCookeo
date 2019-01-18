package com.rukiasoft.androidapps.cocinaconroll.ui.recipelist

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.animation.AccelerateDecelerateInterpolator
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
                    /*Window window = mActivity.getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(ContextCompat.getColor(mActivity, R.color.ColorPrimaryDark));*/
                    if (animate) {
                        binding.toolbarRecipeListFragment.addOnLayoutChangeListener(object :
                            View.OnLayoutChangeListener {
                            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
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
                                    mOpenCircleRevealX,
                                    mOpenCircleRevealY,
                                    Math.hypot(
                                        binding.toolbarRecipeListFragment.width.toDouble(),
                                        binding.toolbarRecipeListFragment.height.toDouble()
                                    ).toFloat(),
                                    0f
                                )

                                // Set a natural ease-in/ease-out interpolator.
                                animator.interpolator = AccelerateDecelerateInterpolator()
                                // make the view invisible when the animation is done
                                animator.addListener(object : AnimatorListenerAdapter() {
                                    override fun onAnimationEnd(animation: Animator) {
                                        super.onAnimationEnd(animation)
                                        binding.toolbarRecipeListFragment.setBackgroundResource(R.color.colorPrimary)
                                    }
                                })

                                // make the view visible and start the animation
                                animator.start()
                            }
                        })
                    } else {
                        binding.toolbarRecipeListFragment.setBackgroundResource(R.color.colorPrimary)
                    }


                    //show the bar and button
                    setVisibilityWithSearchWidget(View.VISIBLE)
                    return true
                }

                override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        animate = true
                        /*Window window = mActivity.getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(ContextCompat.getColor(mActivity, R.color.ColorPrimarySearchDark));*/
                        binding.toolbarRecipeListFragment.addOnLayoutChangeListener(object :
                            View.OnLayoutChangeListener {
                            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
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
                                    Math.hypot(binding.toolbarRecipeListFragment.width.toDouble(),
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
                    }
                    binding.toolbarRecipeListFragment.setBackgroundResource(R.color.colorPrimarySearch)
                    //hide the bar and button
                    setVisibilityWithSearchWidget(View.GONE)
                    //hide the floating button

                    return true
                }

            })
            val searchView = searchMenuItem?.actionView
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
        val icon: Int
        val text: String
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
            MainViewModel.FilterType.BY_NAME -> {
                icon = R.drawable.ic_search_white_24dp
                text = resourcesManager.getString(R.string.by_name)
            }
        }
        binding.iconId = icon
        binding.recipeListTypeRecipes.text = text
        binding.executePendingBindings()
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
