package com.rukiasoft.androidapps.cocinaconroll.ui.recipedetails

import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnticipateOvershootInterpolator
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.rukiasoft.androidapps.cocinaconroll.CocinaConRollApplication
import com.rukiasoft.androidapps.cocinaconroll.R
import com.rukiasoft.androidapps.cocinaconroll.databinding.RecipeDetailsFragmentBinding
import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Recipe
import com.rukiasoft.androidapps.cocinaconroll.ui.common.BaseFragment
import com.rukiasoft.androidapps.cocinaconroll.ui.common.MainActivity
import javax.inject.Inject


class RecipeDetailsFragment : BaseFragment(), AppBarLayout.OnOffsetChangedListener {


    private lateinit var viewModel: RecipeDetailsViewModel
    private lateinit var binding: RecipeDetailsFragmentBinding
    private lateinit var ingredientsAdapter: RecipeDetailsAdapter
    private lateinit var stepsAdapter: RecipeDetailsAdapter
    private lateinit var transitionName: String
    private var colorClear: Int = 0
    private var colorDark: Int = 0

    @Inject
    lateinit var application: CocinaConRollApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition()
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(R.transition.recipe_image_transition)

        ingredientsAdapter = RecipeDetailsAdapter(cookeoBindingComponent)
        stepsAdapter = RecipeDetailsAdapter(cookeoBindingComponent)
        arguments?.apply {
            val safeArgs = RecipeDetailsFragmentArgs.fromBundle(this)
            transitionName = safeArgs.trnansitionName
            colorClear = safeArgs.colorClear
            colorDark = safeArgs.colorDark
        }
    }

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
        ViewCompat.setTransitionName(binding.recipePic, transitionName)
        applyPalette()

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.recipeDetailsCards.apply {
            listviewIngredients.apply {
                adapter = ingredientsAdapter
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                ViewCompat.setNestedScrollingEnabled(this, false)
            }
            listviewSteps.apply {
                adapter = stepsAdapter
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                ViewCompat.setNestedScrollingEnabled(this, false)
            }
        }
        binding.appbarlayoutRecipeDetails.addOnOffsetChangedListener(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(RecipeDetailsViewModel::class.java)
        arguments?.apply {
            val safeArgs = RecipeDetailsFragmentArgs.fromBundle(this)
            viewModel.loadRecipeFromDb(safeArgs.recipeKey)
        }

        viewModel.getRecipe().observe(this, Observer { recipe ->
            recipe?.let {
                startPostponedEnterTransition()
                binding.recipe = it
                setAuthor(recipe.recipe)
                binding.recipeDescriptionFab.setImageResource(
                    if (recipe.recipe.favourite) {
                        R.drawable.ic_favorite_white_24dp
                    } else {
                        R.drawable.ic_favorite_outline_white_24dp
                    }
                )

                binding.recipeNameRecipeDetails.text = recipe.recipe.name

                binding.recipeDescriptionFab.refreshDrawableState()
                ingredientsAdapter.updateItems(recipe.ingredients ?: listOf())
                stepsAdapter.updateItems(recipe.steps ?: listOf())
                binding.collapsingToolbarRecipeDetails.title = recipe.recipe.name


            }
        })

        binding.recipeDescriptionFab.setOnClickListener {
            clickOnHeartButton()
        }

        (activity as? MainActivity)?.setToolbar(binding.toolbarRecipeDetails, false)
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.keepScreenOn(screenOn = true)
    }

    override fun onPause() {
        super.onPause()
        (activity as? MainActivity)?.keepScreenOn(screenOn = false)
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
                @Suppress("DEPRECATION")
                Html.fromHtml(link)
            }
            binding.recipeDetailsCards.cardviewLinkTextview.text = linkFormatted
            binding.recipeDetailsCards.cardviewLinkTextview.movementMethod = LinkMovementMethod.getInstance()
        }

    }

    private fun applyPalette() {

        (activity as? MainActivity)?.updateStatusBar(colorDark)

        binding.collapsingToolbarRecipeDetails.contentScrim = ColorDrawable(colorDark)

        binding.recipeDescriptionFab.backgroundTintList = ColorStateList(
            arrayOf(intArrayOf(0)),
            intArrayOf(colorClear)
        )

        if(viewUtils.needToSetStatusBarThemeAsDark(colorDark)){
            binding.toolbarRecipeDetails.context.setTheme(R.style.CocinaConRollActionBarThemeClearIcon)
            binding.collapsingToolbarRecipeDetails.setCollapsedTitleTextColor(resourcesManager.getColor(android.R.color.white))
        }else{
            binding.toolbarRecipeDetails.context.setTheme(R.style.CocinaConRollActionBarThemeDarkIcon)
            binding.collapsingToolbarRecipeDetails.setCollapsedTitleTextColor(resourcesManager.getColor(R.color.ColorDarkText))
        }
    }

    private val scaleIn = Runnable {
        binding.recipeDescriptionFab.animate().setDuration(250)
            .setInterpolator(AnticipateOvershootInterpolator())
            .scaleX(1.2f)
            .scaleY(1.2f)
            .withEndAction(scaleOut)
    }

    private val scaleOut = Runnable {
        binding.recipeDescriptionFab.animate().setDuration(250)
            .setInterpolator(AnticipateOvershootInterpolator())
            .scaleX(1.0f)
            .scaleY(1.0f)
    }

    private fun clickOnHeartButton() {
        viewModel.getRecipe().value?.recipe?.let { recipe ->
            appExecutors.diskIO().execute {
                persistenceManager.setFavourite(recipe.recipeKey, recipe.favourite.not())
            }

            scaleIn.run()

        }
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, offset: Int) {

        val maxScroll = appBarLayout.totalScrollRange
        val percentage = Math.abs(offset).toFloat() / maxScroll.toFloat()
        handleTitleBehavior(percentage)

    }

    private fun handleTitleBehavior(percentage: Float) {
        if (percentage >= PERCENTAGE_TO_ELLIPSIZE_TITLE) {
            binding.recipeNameRecipeDetails.visibility = View.GONE
        } else {
            binding.recipeNameRecipeDetails.visibility = View.VISIBLE
            binding.recipeNameRecipeDetails.alpha = 1 - percentage / PERCENTAGE_TO_ELLIPSIZE_TITLE
        }
    }

    companion object {
        private const val PERCENTAGE_TO_ELLIPSIZE_TITLE = 0.1f
    }
}
