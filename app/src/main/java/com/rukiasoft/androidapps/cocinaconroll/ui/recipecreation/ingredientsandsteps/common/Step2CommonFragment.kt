package com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation.ingredientsandsteps.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.rukiasoft.androidapps.cocinaconroll.R
import com.rukiasoft.androidapps.cocinaconroll.databinding.FragmentStep2Binding
import com.rukiasoft.androidapps.cocinaconroll.persistence.relations.RecipeWithInfo
import com.rukiasoft.androidapps.cocinaconroll.ui.common.MainActivity
import com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation.ChildBaseFragment


abstract class Step2CommonFragment : ChildBaseFragment() {

    protected lateinit var binding: FragmentStep2Binding
    protected lateinit var adapter: EditRecipeAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_step2, container, false, cookeoBindingComponent)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = EditRecipeAdapter()
        binding.editRecipeRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            this@apply.adapter = this@Step2CommonFragment.adapter
        }

        listener.getRecipe().observe(this, Observer {
            it?.let { recipe ->
                adapter.submitList(getListOfItems(recipe))
            }
        })

        binding.editRecipeAddItem.setText(getItemsInEditTextBox())

        binding.editRecipeAddFab.setOnClickListener {
            addItemToRecipe()
        }
    }

    abstract fun getListOfItems(recipeWithInfo: RecipeWithInfo): List<String>?
    abstract fun getItemsInEditTextBox(): String

    private fun addItemToRecipe() {
        if (binding.editRecipeAddItem.text?.isNotBlank() == true) {
            val list: MutableList<String> = adapter.currentList.toMutableList()
            list.add(binding.editRecipeAddItem.text.toString())
            adapter.submitList(list)
            binding.editRecipeAddItem.text = null
            (activity as? MainActivity)?.hideKeyboard()
        }
    }


}
