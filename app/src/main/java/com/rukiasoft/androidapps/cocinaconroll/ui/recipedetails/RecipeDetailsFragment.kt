package com.rukiasoft.androidapps.cocinaconroll.ui.recipedetails

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.transition.TransitionInflater
import android.view.*
import android.view.animation.AnticipateOvershootInterpolator
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.rukiasoft.androidapps.cocinaconroll.NavGraphDirections
import com.rukiasoft.androidapps.cocinaconroll.R
import com.rukiasoft.androidapps.cocinaconroll.databinding.RecipeDetailsFragmentBinding
import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Recipe
import com.rukiasoft.androidapps.cocinaconroll.persistence.relations.RecipeWithInfo
import com.rukiasoft.androidapps.cocinaconroll.ui.common.BaseFragment
import com.rukiasoft.androidapps.cocinaconroll.ui.common.MainActivity
import kotlinx.coroutines.*


@ExperimentalCoroutinesApi
class RecipeDetailsFragment : BaseFragment(), AppBarLayout.OnOffsetChangedListener, CoroutineScope by MainScope() {


    private lateinit var viewModel: RecipeDetailsViewModel
    private lateinit var binding: RecipeDetailsFragmentBinding
    private lateinit var ingredientsAdapter: RecipeDetailsAdapter
    private lateinit var stepsAdapter: RecipeDetailsAdapter
    private lateinit var transitionName: String
    private var colorClear: Int = 0
    private var colorDark: Int = 0
    private lateinit var recipeWithAllInfo: RecipeWithInfo


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        postponeEnterTransition()
        enterTransition =
            TransitionInflater.from(context).inflateTransition(R.transition.recipe_enter_transition)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(R.transition.recipe_image_transition)
        returnTransition =
            TransitionInflater.from(context).inflateTransition(R.transition.detail_window_return_transition)

        ingredientsAdapter = RecipeDetailsAdapter(cookeoBindingComponent)
        stepsAdapter = RecipeDetailsAdapter(cookeoBindingComponent)
        arguments?.apply {
            val safeArgs = RecipeDetailsFragmentArgs.fromBundle(this)
            transitionName = safeArgs.transitionName
            colorClear = safeArgs.colorClear
            colorDark = safeArgs.colorDark
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_recipe_details, menu)
        if (viewUtils.needToSetStatusBarThemeAsDark(colorDark).not() || deviceUtils.isPortrait().not()) {
            for (i in 0 until menu.size()) {
                val item: MenuItem = menu.getItem(i)
                val newIcon: Drawable = item.icon
                newIcon.mutate().setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY)
                item.icon = newIcon
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        viewModel.getRecipe().value?.recipe?.let { recipe ->
            menu.findItem(R.id.menu_item_remove).isVisible =
                recipe.edited or recipe.personal
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_edit_recipe -> findNavController().navigate(
                NavGraphDirections.actionGlobalNewRecipeContainerFragment(
                    recipeWithAllInfo.recipe.recipeKey
                )
            )
        }
        return super.onOptionsItemSelected(item)
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
        binding.appbarlayoutRecipeDetails?.addOnOffsetChangedListener(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(RecipeDetailsViewModel::class.java)
        arguments?.apply {
            val safeArgs = RecipeDetailsFragmentArgs.fromBundle(this)
            viewModel.loadRecipeFromDb(safeArgs.recipeKey)
        }

        viewModel.getRecipe().observe(this, Observer { recipe ->
            recipe?.let {
                activity?.invalidateOptionsMenu()
                startPostponedEnterTransition()
                recipeWithAllInfo = it
                viewModel.getRecipe().removeObservers(this)
                binding.recipe = recipeWithAllInfo
                setAuthor(recipeWithAllInfo.recipe)
                setButtonResource()

                binding.recipeNameRecipeDetails.text = recipeWithAllInfo.recipe.name

                binding.recipeDescriptionFab.refreshDrawableState()
                ingredientsAdapter.updateItems(recipeWithAllInfo.ingredients ?: listOf())
                stepsAdapter.updateItems(recipeWithAllInfo.steps ?: listOf())
                binding.collapsingToolbarRecipeDetails?.title = recipeWithAllInfo.recipe.name
                launch {
                    delay(1000)
                    withContext(Dispatchers.Main) { binding.recipeDescriptionFab.show() }
                }

            }
        })

        binding.recipeDescriptionFab.setOnClickListener {
            clickOnHeartButton()
        }

        (activity as? MainActivity)?.setToolbar(binding.toolbarRecipeDetails, false)
    }

    private fun setButtonResource() {
        binding.recipeDescriptionFab.setImageResource(
            if (recipeWithAllInfo.recipe.favourite) {
                R.drawable.ic_favorite_white_24dp
            } else {
                R.drawable.ic_favorite_outline_white_24dp
            }
        )
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

        launch {
            val sAuthor = resources.getString(R.string.default_author)
            if (recipe.author.equals(sAuthor) || recipe.link.isNullOrBlank()) {
                val author = viewModel.getDefaultAuthorFormatted()
                binding.recipeDetailsCards.cardviewLinkTextview.text = author
            } else {
                val linkFormatted: Spanned? = viewModel.getLinkAuthorFormatted(recipe)
                binding.recipeDetailsCards.cardviewLinkTextview.text = linkFormatted
                binding.recipeDetailsCards.cardviewLinkTextview.movementMethod = LinkMovementMethod.getInstance()
            }
        }

    }

    private fun applyPalette() {

        (activity as? MainActivity)?.updateStatusBar(colorDark)

        binding.collapsingToolbarRecipeDetails?.contentScrim = ColorDrawable(colorDark)

        binding.recipeDescriptionFab.backgroundTintList = ColorStateList(
            arrayOf(intArrayOf(0)),
            intArrayOf(colorClear)
        )

        if (deviceUtils.isPortrait().not()) return

        if (viewUtils.needToSetStatusBarThemeAsDark(colorDark)) {
            binding.toolbarRecipeDetails.context.setTheme(R.style.CocinaConRollActionBarThemeClearIcon)
            binding.collapsingToolbarRecipeDetails?.setCollapsedTitleTextColor(resourcesManager.getColor(android.R.color.white))
        } else {
            binding.toolbarRecipeDetails.context.setTheme(R.style.CocinaConRollActionBarThemeDarkIcon)
            binding.collapsingToolbarRecipeDetails?.setCollapsedTitleTextColor(resourcesManager.getColor(R.color.ColorDarkText))
            binding.recipePicProtection?.background =
                resourcesManager.getDrawable(R.drawable.photo_background_protection_white)
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
        recipeWithAllInfo.toggleFavourite()
        setButtonResource()
        scaleIn.run()
    }

    override fun onDestroy() {
        enterTransition = null
        sharedElementEnterTransition = null
        super.onDestroy()
        GlobalScope.launch(Dispatchers.IO) {
            persistenceManager.setFavourite(
                recipeWithAllInfo.recipe.recipeKey,
                recipeWithAllInfo.recipe.favourite
            )
        }
        cancel()
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, offset: Int) {
        if (deviceUtils.isPortrait()) {
            val maxScroll = appBarLayout.totalScrollRange
            val percentage = Math.abs(offset).toFloat() / maxScroll.toFloat()
            handleTitleBehavior(percentage)
        } else {
            handleTitleBehavior(0f)
        }

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
