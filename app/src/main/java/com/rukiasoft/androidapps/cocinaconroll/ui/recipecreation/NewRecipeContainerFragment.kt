package com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.*
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.rukiasoft.androidapps.cocinaconroll.NavGraphDirections
import com.rukiasoft.androidapps.cocinaconroll.R
import com.rukiasoft.androidapps.cocinaconroll.databinding.NewRecipeContainerFragmentBinding
import com.rukiasoft.androidapps.cocinaconroll.persistence.relations.RecipeWithInfo
import com.rukiasoft.androidapps.cocinaconroll.ui.common.BaseFragment
import com.rukiasoft.androidapps.cocinaconroll.ui.common.MainActivity
import com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation.ingredientsandsteps.ingredients.Step2IngredientsFragmentDirections
import com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation.ingredientsandsteps.steps.Step2StepsFragmentDirections
import com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation.maindata.Step1FragmentDirections
import kotlinx.coroutines.*


class NewRecipeContainerFragment : BaseFragment(), NewRecipeParent, CoroutineScope by MainScope() {

    private lateinit var binding: NewRecipeContainerFragmentBinding

    private lateinit var viewModel: NewRecipeContainerViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.new_recipe_container_fragment,
            container,
            false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(NewRecipeContainerViewModel::class.java)
        if (savedInstanceState == null) {
            arguments?.apply {
                val safeArgs = NewRecipeContainerFragmentArgs.fromBundle(this)
                safeArgs.recipeKey?.let { key ->
                    launch(Dispatchers.IO) {
                        viewModel.getRecipe()
                            .postValue(persistenceManager.getRecipeWithAllInfo(key))
                    }
                }
                arguments?.keySet()?.forEach {
                    remove(it)
                }
            }
        }
        (activity as? MainActivity)?.apply {
            setToolbar(
                binding.toolbarNewRecipeRagment,
                false,
                null
            )
        }

        val targetView = getViewByPosition(viewModel.selectedPosition)
        targetView.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                targetView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                setFragmentSelected(viewModel.selectedPosition)
            }
        })

    }

    private fun setSelectedDot(position: NewRecipeParent.ChildPosition) {
        val targetView = getViewByPosition(position)
        binding.selectedDot.x = targetView.x
        binding.selectedDot.y = targetView.y
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.new_recipe, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return if (item.itemId != android.R.id.home && getCurrentFragment()?.validateFields() == true) {
            when (item.itemId) {
                R.id.action_next -> {
                    activity?.let {
                        when (Navigation.findNavController(
                            it,
                            R.id.fragment_new_recipe_container
                        ).currentDestination?.id) {
                            R.id.step1Fragment -> Navigation.findNavController(
                                it,
                                R.id.fragment_new_recipe_container
                            ).navigate(
                                Step1FragmentDirections.actionStep1FragmentToStep2IngredientsFragment()
                            )
                            R.id.step2IngredientsFragment -> Navigation.findNavController(
                                it,
                                R.id.fragment_new_recipe_container
                            ).navigate(
                                Step2IngredientsFragmentDirections.actionStep2IngredientsFragmentToStep2StepsFragment()
                            )
                            R.id.step2StepsFragment -> Navigation.findNavController(
                                it,
                                R.id.fragment_new_recipe_container
                            ).navigate(
                                Step2StepsFragmentDirections.actionStep2StepsFragmentToStep2TipFragment()
                            )
                            R.id.step2TipFragment -> {
                                viewModel.saveRecipe()
                                findNavController().navigate(
                                    NavGraphDirections.actionGlobalRecipeListFragment()
                                )
                            }
                        }
                    }
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun animateFromPosition(
        initialPosition: NewRecipeParent.ChildPosition,
        finalPosition: NewRecipeParent.ChildPosition
    ) {


        val animateToRight = finalPosition.position > initialPosition.position
        val initialDot = getViewByPosition(initialPosition)
        val finalDot = getViewByPosition(finalPosition)
        val maxWidth = viewModel.getMaxWidth(
            initialX = initialDot.x,
            initialWidth = initialDot.width,
            finalX = finalDot.x,
            finalWidth = finalDot.width,
            animateToRight = animateToRight
        )
        ValueAnimator.ofInt(initialDot.width, maxWidth).apply {
            duration =
                (resourcesManager.getInteger(android.R.integer.config_mediumAnimTime) / 2).toLong()
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener { animation ->
                val animatedValue = animation.animatedValue as Int
                binding.selectedDot.layoutParams.width = animatedValue
                binding.selectedDot.x = if (animateToRight) {
                    initialDot.x
                } else {
                    initialDot.x + initialDot.width - animatedValue
                }
                binding.selectedDot.requestLayout()
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    ValueAnimator.ofInt(maxWidth, finalDot.width).apply {
                        duration =
                            (resourcesManager.getInteger(android.R.integer.config_mediumAnimTime) / 2).toLong()
                        interpolator = AccelerateDecelerateInterpolator()
                        addUpdateListener { animation ->
                            val animatedValue = animation.animatedValue as Int
                            binding.selectedDot.layoutParams.width = animatedValue
                            binding.selectedDot.x = if (animateToRight) {
                                finalDot.x + finalDot.width - animatedValue
                            } else {
                                finalDot.x
                            }
                            binding.selectedDot.requestLayout()
                        }
                        start()
                    }
                }
            })
            start()
        }
    }

    private fun getCurrentFragment(): ChildBaseFragment? {
        val navHost = childFragmentManager.findFragmentById(R.id.fragment_new_recipe_container)
        val fragment =
            navHost?.childFragmentManager?.findFragmentById(R.id.fragment_new_recipe_container)
        return fragment as? ChildBaseFragment
    }

    private fun getViewByPosition(childPosition: NewRecipeParent.ChildPosition): ImageView {
        return when (childPosition) {

            NewRecipeParent.ChildPosition.FIRST -> binding.firstDot
            NewRecipeParent.ChildPosition.SECOND -> binding.secondDot
            NewRecipeParent.ChildPosition.THIRD -> binding.thirdDot
            NewRecipeParent.ChildPosition.FOURTH -> binding.fourthDot
        }
    }

    override fun setFragmentSelected(childPosition: NewRecipeParent.ChildPosition) {
        if (childPosition != viewModel.selectedPosition) {
            animateFromPosition(
                initialPosition = viewModel.selectedPosition,
                finalPosition = childPosition
            )
            viewModel.selectedPosition = childPosition
        } else {
            setSelectedDot(childPosition)
        }
    }

    override fun getRecipe(): LiveData<RecipeWithInfo> = viewModel.getRecipe()

    override fun setStep1(
        name: String,
        picture: String,
        minutes: String?,
        portions: String?,
        type: String,
        vegetarian: Boolean
    ) {
        viewModel.setStep1(name, picture, minutes, portions, type, vegetarian)

    }

    override fun setIngredients(ingredients: List<String>) {
        viewModel.setIngredients(ingredients)
    }

    override fun saveIngredientInBox(ingredient: String) {
        viewModel.ingredientInBox = ingredient
    }

    override fun getIngredientInBox(): String = viewModel.ingredientInBox

    override fun setSteps(steps: List<String>) {
        viewModel.setSteps(steps)
    }

    override fun saveStepInBox(step: String) {
        viewModel.stepInBox = step
    }

    override fun getStepInBox(): String = viewModel.stepInBox

    override fun getTip(): String? =
        getRecipe().value?.recipe?.tip

    override fun saveTip(tip: String) {
        getRecipe().value?.recipe?.tip = tip
    }

    override fun onDestroy() {
        cancel()
        super.onDestroy()
    }
}
