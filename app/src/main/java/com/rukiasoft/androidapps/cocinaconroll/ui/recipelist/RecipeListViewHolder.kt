package com.rukiasoft.androidapps.cocinaconroll.ui.recipelist

import android.animation.Animator
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.rukiasoft.androidapps.cocinaconroll.R
import com.rukiasoft.androidapps.cocinaconroll.databinding.RecipeItemBinding
import com.rukiasoft.androidapps.cocinaconroll.persistence.PersistenceManager
import com.rukiasoft.androidapps.cocinaconroll.resources.ResourcesManager
import com.rukiasoft.androidapps.cocinaconroll.utils.AppExecutors


class RecipeListViewHolder(
    private val binding: RecipeItemBinding,
    private val resourceManager: ResourcesManager,
    private val persistenceManager: PersistenceManager,
    private val appExecutors: AppExecutors,
    private val clickRecipeListener: RecipeListAdapter.OnRecipeClicked,
    private val extractRecipeListener: RecipeListAdapter.ExtractRecipe
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(recipeKey: String) {
        extractRecipeListener.getRecipe(recipeKey)?.let { recipe ->
            binding.recipe = recipe
            ViewCompat.setTransitionName(binding.recipePicCardview, recipe.recipeKey)

            binding.cardviewRecipeItem.apply {
                if (recipe.rotated && rotationY == 0f) {
                    rotationY = 180f
                    binding.frontCardviewRecipeItem.alpha = 0f
                    binding.backCardviewRecipeItem.alpha = 1f
                    binding.backCardviewRecipeItem.visibility = View.VISIBLE
                } else if (!recipe.rotated && rotationY != 0f) {
                    rotationY = 0f
                    binding.frontCardviewRecipeItem.alpha = 1f
                    binding.backCardviewRecipeItem.alpha = 0f
                    binding.backCardviewRecipeItem.visibility = View.INVISIBLE
                }
                recipe.let {
                    binding.likeButton.initialize(
                        recipe,
                        binding.recipeItemFavoriteIcon,
                        persistenceManager,
                        appExecutors,
                        recipe.rotated
                    )
                }

                if(hasOnClickListeners()){
                    setOnClickListener(null)
                }
                setOnClickListener {
                    val position = if(adapterPosition != RecyclerView.NO_POSITION) adapterPosition else layoutPosition
                    extractRecipeListener.getRecipe(position)?.let { recipeClicked ->
                        clickRecipeListener.recipeSelected(recipe = recipeClicked, view = binding.recipePicCardview)
                    }
                }
                setOnLongClickListener { card ->
                    val position = if(adapterPosition != RecyclerView.NO_POSITION) adapterPosition else layoutPosition
                    extractRecipeListener.getRecipe(position)?.let { recipeClicked ->

                        val flipDuration: Long = resourceManager.getInteger(R.integer.card_flip_time_half).toLong()
                        val halfDuration: Long = flipDuration / 2
                        val front: View
                        val back: View
                        if (card.rotationY == 0f) {
                            front = binding.frontCardviewRecipeItem
                            back = binding.backCardviewRecipeItem
                            binding.backCardviewRecipeItem.visibility = View.VISIBLE
                            binding.likeButton.setClick(true)
                        } else {
                            back = binding.frontCardviewRecipeItem
                            front = binding.backCardviewRecipeItem
                            Handler(Looper.getMainLooper()).postDelayed({
                                binding.backCardviewRecipeItem.visibility = View.GONE
                            }, flipDuration)
                            binding.likeButton.setClick(false)
                        }
                      card.animate()
                            .setDuration(flipDuration)
                            .setListener(object : Animator.AnimatorListener {
                                override fun onAnimationRepeat(p0: Animator?) {}

                                override fun onAnimationEnd(p0: Animator?) {
                                    recipeClicked.rotated = recipeClicked.rotated.not()
                                    if (recipeClicked.rotated.not()) {
                                        clickRecipeListener.updateCard(position)
                                    }
                                }

                                override fun onAnimationCancel(p0: Animator?) {}

                                override fun onAnimationStart(p0: Animator?) {}

                            })
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

}