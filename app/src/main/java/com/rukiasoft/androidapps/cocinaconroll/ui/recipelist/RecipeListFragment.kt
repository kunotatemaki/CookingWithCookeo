package com.rukiasoft.androidapps.cocinaconroll.ui.recipelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rukiasoft.androidapps.cocinaconroll.R
import com.rukiasoft.androidapps.cocinaconroll.databinding.FragmentRecipeListBinding
import com.rukiasoft.androidapps.cocinaconroll.ui.common.BaseFragment
import com.rukiasoft.androidapps.cocinaconroll.ui.common.MainActivity

class RecipeListFragment : BaseFragment(), RecipeListAdapter.OnRecipeClicked {


    private lateinit var viewModel: RecipeListViewModel

    private lateinit var binding: FragmentRecipeListBinding

    private lateinit var adapter: RecipeListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.recipe_list_fragment, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(RecipeListViewModel::class.java)

        adapter = RecipeListAdapter(this)
        binding.recipeList.adapter = adapter
        binding.recipeList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        (activity as? MainActivity)?.getPersistenceViewModel()?.let { persistenceViewModel ->
            persistenceViewModel.getListOfRecipes().observe(this, Observer { it ->
                it?.let {
                    adapter.submitList(it)
                }
            })

        }

    }

    override fun recipeSelected(recipeKey: String?) {
        recipeKey?.let {
            findNavController().navigate(RecipeListFragmentDirections.actionRecipeListFragmentToRecipeDetailsFragment(it))
        }
    }
}
