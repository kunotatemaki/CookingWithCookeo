package com.rukiasoft.androidapps.cocinaconroll.ui.recipelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rukiasoft.androidapps.cocinaconroll.R
import com.rukiasoft.androidapps.cocinaconroll.databinding.FragmentRecipeListBinding
import com.rukiasoft.androidapps.cocinaconroll.ui.common.BaseFragment
import timber.log.Timber

class RecipeListFragment : BaseFragment() {


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

        binding.testButton.setOnClickListener {
            viewModel.filter()
        }

        adapter = RecipeListAdapter()
        binding.recipeList.adapter = adapter
        binding.recipeList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        viewModel.getListOfRecipes().observe(this, Observer { it ->
            it?.let {
                Timber.d("")
                adapter.submitList(it)
            }
        })

    }

}
