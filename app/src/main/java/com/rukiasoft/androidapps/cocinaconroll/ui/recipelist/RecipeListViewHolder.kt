package com.rukiasoft.androidapps.cocinaconroll.ui.recipelist

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.rukiasoft.androidapps.cocinaconroll.R
import com.rukiasoft.androidapps.cocinaconroll.databinding.RecipeItemBinding
import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Recipe
import com.rukiasoft.androidapps.cocinaconroll.resources.ResourcesManager


class RecipeListViewHolder(private val binding: RecipeItemBinding, private val resourceManager: ResourcesManager) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(recipe: Recipe?, listener: RecipeListAdapter.OnRecipeClicked) {
        binding.recipe = recipe

        recipe?.let {
            binding.likeButton.initialize(recipe, binding.recipeItemFavoriteIcon)
        }

        binding.cardviewRecipeItem.apply {
            if (recipe?.rotated == true && rotationY == 0f){
                rotationY = 180f
                binding.frontCardviewRecipeItem.alpha = 0f
                binding.backCardviewRecipeItem.alpha = 1f
            }else if(recipe?.rotated == false && rotationY != 0f){
                rotationY = 0f
                binding.frontCardviewRecipeItem.alpha = 1f
                binding.backCardviewRecipeItem.alpha = 0f
            }

            setOnClickListener {
                listener.recipeSelected(recipeKey = recipe?.recipeKey)
            }
            setOnLongClickListener { card ->
                recipe?.let {
                    val flipDuration: Long = resourceManager.getInteger(R.integer.card_flip_time_half).toLong()
                    val halfDuration: Long = flipDuration / 2
                    val front: View
                    val back: View
                    if (it.rotated.not()) {
                        front = binding.frontCardviewRecipeItem
                        back = binding.backCardviewRecipeItem
                    } else {
                        back = binding.frontCardviewRecipeItem
                        front = binding.backCardviewRecipeItem
                    }
                    it.rotated = it.rotated.not()
                    card.animate()
                        .setDuration(flipDuration)
                        .rotationYBy(180f)
                        .start()
                    front.animate()
                        .setDuration(halfDuration)
                        .alpha(0f)
                        .start()
                    back.animate()
                        .setDuration(halfDuration)
                        .setStartDelay(halfDuration)
                        .alpha(1f)
                        .start()
                }
                true
            }
        }
        binding.executePendingBindings()
    }


}