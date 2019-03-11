package com.rukiasoft.androidapps.cocinaconroll.ui.recipedetails

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.rukiasoft.androidapps.cocinaconroll.R
import com.rukiasoft.androidapps.cocinaconroll.databinding.RecipeDetailsFragmentBinding
import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Recipe
import com.rukiasoft.androidapps.cocinaconroll.ui.common.BaseFragment

class RecipeDetailsFragment : BaseFragment() {


    private lateinit var viewModel: RecipeDetailsViewModel
    private lateinit var binding: RecipeDetailsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.recipe_details_fragment,
            container,
            false,
            cookeoBindingComponent
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(RecipeDetailsViewModel::class.java)
        arguments?.apply {
            val safeArgs = RecipeDetailsFragmentArgs.fromBundle(this)
            viewModel.loadRecipeFromDb(safeArgs.recipeKey)
        }

        viewModel.getRecipe().observe(this, Observer { recipe ->
            recipe?.let {
                val text = viewModel.getRecipeAsString(recipe)
                binding.recipe = it
                setAuthor(recipe.recipe)
            }
        })

    }

    private fun setAuthor(recipe: Recipe) {
        val sAuthor = resources.getString(R.string.default_author)
        if (recipe.author.equals(sAuthor) || recipe.link.isNullOrBlank()) {
            val author =
                "${resourcesManager.getString(R.string.author)} ${resources.getString(R.string.default_author)}"
            binding.recipeDetailsCards.cardviewLinkTextview.text = author
        } else {
            val link =
                "${resourcesManager.getString(R.string.original_link)} <a href=\"${recipe.link}\">${recipe.author}</a>"
            val linkFormatted: Spanned
            linkFormatted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(link, Html.FROM_HTML_MODE_LEGACY)
            } else {
                Html.fromHtml(link)
            }
            binding.recipeDetailsCards.cardviewLinkTextview.text = linkFormatted
            binding.recipeDetailsCards.cardviewLinkTextview.movementMethod = LinkMovementMethod.getInstance()
        }

    }

}
