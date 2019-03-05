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
import com.rukiasoft.androidapps.cocinaconroll.resources.ResourcesManager

class RecipeListAdapter constructor(private val listener: OnRecipeClicked,
                                    private val cookeoBindingComponent: CookeoBindingComponent,
                                    private val resourcesManager: ResourcesManager): PagedListAdapter<Recipe, RecipeListViewHolder>(diffCallback) {

    interface OnRecipeClicked{
        fun recipeSelected(recipeKey: String?)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<RecipeItemBinding>(inflater, R.layout.recipe_item, parent, false, cookeoBindingComponent)

        return RecipeListViewHolder(binding = binding, resourceManager = resourcesManager)
    }

    override fun onBindViewHolder(holder: RecipeListViewHolder, position: Int) {
        val recipe: Recipe? = getItem(position)
        holder.bind(recipe, listener)
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