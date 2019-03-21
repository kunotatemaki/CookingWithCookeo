package com.rukiasoft.androidapps.cocinaconroll.ui.recipedetails

import androidx.recyclerview.widget.RecyclerView
import com.rukiasoft.androidapps.cocinaconroll.databinding.IngredientItemBinding
import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Ingredient

class IngredientViewHolder(
    private val binding: IngredientItemBinding
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(ingredient: Ingredient) {
        binding.ingredient = ingredient
    }

}