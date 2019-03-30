package com.rukiasoft.androidapps.cocinaconroll.ui.recipelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.rukiasoft.androidapps.cocinaconroll.R
import com.rukiasoft.androidapps.cocinaconroll.databinding.CookeoBindingComponent
import com.rukiasoft.androidapps.cocinaconroll.databinding.RecipeItemBinding
import com.rukiasoft.androidapps.cocinaconroll.persistence.PersistenceManager
import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Recipe
import com.rukiasoft.androidapps.cocinaconroll.resources.ResourcesManager
import com.rukiasoft.androidapps.cocinaconroll.utils.AppExecutors
import timber.log.Timber

class RecipeListAdapter constructor(
    private val listener: OnRecipeClicked,
    private val cookeoBindingComponent: CookeoBindingComponent,
    private val resourcesManager: ResourcesManager,
    private val persistenceManager: PersistenceManager,
    private val appExecutors: AppExecutors
) : PagedListAdapter<Recipe, RecipeListViewHolder>(diffCallback) {

    interface OnRecipeClicked {
        fun recipeSelected(recipe: Recipe?, view: View)
        fun updateCard(position: Int)
    }

    interface ExtractRecipe {
        fun getRecipe(recipeKey: String): Recipe?
        fun getRecipe(position: Int): Recipe?
    }

    private val extractRecipe: ExtractRecipe = object : ExtractRecipe {

        override fun getRecipe(recipeKey: String): Recipe? {
            for (i in 0 until itemCount) {
                if (getItem(i)?.recipeKey == recipeKey) {
                    return getItem(i)
                }
            }
            return null
        }

        override fun getRecipe(position: Int): Recipe? {
            return if(position in 0 until itemCount){
                getItem(position)
            } else{
                null
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<RecipeItemBinding>(
            inflater,
            R.layout.recipe_item,
            parent,
            false,
            cookeoBindingComponent
        )

        return RecipeListViewHolder(
            binding = binding,
            resourceManager = resourcesManager,
            persistenceManager = persistenceManager,
            appExecutors = appExecutors,
            extractRecipeListener = extractRecipe,
            clickRecipeListener = listener
        )
    }

    override fun onBindViewHolder(holder: RecipeListViewHolder, position: Int) {
        val recipe: Recipe? = getItem(position)
        holder.bind(recipe?.recipeKey ?: "")
    }


    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Recipe>() {
            override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
                if (newItem.recipeKey == "-KhOKFZs_bK8KHVD8z1y") {
                    Timber.d("")
                }
                return oldItem.recipeKey == newItem.recipeKey
            }

            override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
                if (newItem.recipeKey == "-KhOKFZs_bK8KHVD8z1y") {
                    Timber.d("")
                }
//                val auxRecipe = newItem.copy(favourite = oldItem.favourite).apply {
//                    rotated = oldItem.rotated
//                }
                newItem.rotated = oldItem.rotated
//                return oldItem == auxRecipe
                return oldItem == newItem
            }
        }
    }

}