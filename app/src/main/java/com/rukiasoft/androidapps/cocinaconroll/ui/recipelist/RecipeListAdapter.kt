package com.rukiasoft.androidapps.cocinaconroll.ui.recipelist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.rukiasoft.androidapps.cocinaconroll.R
import com.rukiasoft.androidapps.cocinaconroll.databinding.CookeoBindingComponent
import com.rukiasoft.androidapps.cocinaconroll.databinding.RecipeItemBinding
import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Recipe

class RecipeListAdapter : PagedListAdapter<Recipe, RecipeListViewHolder>(diffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<RecipeItemBinding>(inflater, R.layout.recipe_item, parent, false, CookeoBindingComponent())

        return RecipeListViewHolder(binding = binding)
    }

    override fun onBindViewHolder(holder: RecipeListViewHolder, position: Int) {
        val recipe: Recipe? = getItem(position)
        holder.bind(recipe)
    }


    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Recipe>() {
            override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean =
                oldItem.recipeKey == newItem.recipeKey

            override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean =
                oldItem == newItem
        }
    }

}