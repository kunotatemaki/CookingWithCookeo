package com.rukiasoft.androidapps.cocinaconroll.ui.recipelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.recipe_list_fragment, container, false, CookeoBindingComponent())

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(RecipeListViewModel::class.java)

        adapter = RecipeListAdapter(this)
        binding.recipeList.adapter = adapter
        binding.recipeList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

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

    private fun submitListToAdapter(list: PagedList<Recipe>) {
        adapter.submitList(list)
        binding.recipeListNumberRecipes.text = String.format(resourcesManager.getString(R.string.recipes), list.size)
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
            findNavController().navigate(RecipeListFragmentDirections.actionRecipeListFragmentToRecipeDetailsFragment(it))
        }
    }
}
