package com.rukiasoft.androidapps.cocinaconroll.ui.recipelist

import androidx.recyclerview.widget.RecyclerView
import com.rukiasoft.androidapps.cocinaconroll.databinding.RecipeItemBinding
import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Recipe

class RecipeListViewHolder(private val binding: RecipeItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(recipe: Recipe?, listener: RecipeListAdapter.OnRecipeClicked) {
        binding.recipeTitle.text = recipe?.name
        binding.recipeItem.setOnClickListener{
            listener.recipeSelected(recipeKey = recipe?.recipeKey)
        }
        binding.executePendingBindings()
    }


}