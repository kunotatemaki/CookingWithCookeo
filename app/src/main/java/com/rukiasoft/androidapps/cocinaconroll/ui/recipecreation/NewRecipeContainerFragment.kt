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
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.rukiasoft.androidapps.cocinaconroll.NavGraphDirections
import com.rukiasoft.androidapps.cocinaconroll.R
import com.rukiasoft.androidapps.cocinaconroll.databinding.NewRecipeContainerFragmentBinding
import com.rukiasoft.androidapps.cocinaconroll.extensions.normalizedString
import com.rukiasoft.androidapps.cocinaconroll.firebase.FirebaseConstants
import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Recipe
import com.rukiasoft.androidapps.cocinaconroll.persistence.relations.RecipeWithInfo
import com.rukiasoft.androidapps.cocinaconroll.persistence.utils.PersistenceConstants
import com.rukiasoft.androidapps.cocinaconroll.ui.common.BaseFragment
import com.rukiasoft.androidapps.cocinaconroll.ui.common.MainActivity
import kotlinx.coroutines.*


@ExperimentalCoroutinesApi
class NewRecipeContainerFragment : BaseFragment(), NewRecipeParent, CoroutineScope by MainScope() {

    private lateinit var binding: NewRecipeContainerFragmentBinding

    private lateinit var viewModel: NewRecipeContainerViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        if (savedInstanceState == null) {
            arguments?.apply {
                val safeArgs = NewRecipeContainerFragmentArgs.fromBundle(this)
                safeArgs.recipeKey?.let { key ->
                    launch(Dispatchers.IO) {
                        viewModel.getRecipe().value = persistenceManager.getRecipeWithAllInfo(key)
                    }
                }
                arguments?.keySet()?.forEach {
                    remove(it)
                }
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.new_recipe_container_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(NewRecipeContainerViewModel::class.java)
        (activity as? MainActivity)?.apply {
            setToolbar(
                binding.toolbarNewRecipeRagment,
                false,
                null
            )
        }

        val targetView = getViewByPosition(viewModel.selectedPosition)
        targetView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
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
        return when (item.itemId) {
            R.id.action_next -> {
                activity?.let {
                    when (Navigation.findNavController(
                        activity!!,
                        R.id.fragment_new_recipe_container
                    ).currentDestination?.id) {
                        R.id.step1Fragment -> Navigation.findNavController(
                            it,
                            R.id.fragment_new_recipe_container
                        ).navigate(Step1FragmentDirections.actionStep1FragmentToStep2Fragment())
                        R.id.step2Fragment -> Navigation.findNavController(
                            it,
                            R.id.fragment_new_recipe_container
                        ).navigate(Step2FragmentDirections.actionStep2FragmentToStep3Fragment())
                        R.id.step3Fragment -> findNavController().navigate(
                            NavGraphDirections.actionGlobalRecipeListFragment()
                        )
                    }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
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
            duration = (resourcesManager.getInteger(android.R.integer.config_mediumAnimTime) / 2).toLong()
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

    private fun getViewByPosition(childPosition: NewRecipeParent.ChildPosition): ImageView {
        return when (childPosition) {

            NewRecipeParent.ChildPosition.FIRST -> binding.firstDot
            NewRecipeParent.ChildPosition.SECOND -> binding.secondDot
            NewRecipeParent.ChildPosition.THIRD -> binding.thirdDot
        }
    }

    override fun setFragmentSelected(childPosition: NewRecipeParent.ChildPosition) {
        if (childPosition != viewModel.selectedPosition) {
            animateFromPosition(initialPosition = viewModel.selectedPosition, finalPosition = childPosition)
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
        val recipe = viewModel.getRecipe().value
        viewModel.setRecipe(recipe?.apply {
            this.recipe.name = name
            this.recipe.normalizedName = name.normalizedString()
            this.recipe.type = type
            this.recipe.icon = Recipe.getIconFromType(type)
            this.recipe.picture = picture
            this.recipe.updatePicture = PersistenceConstants.FLAG_NOT_UPDATE_PICTURE
            this.recipe.vegetarian = vegetarian
            this.recipe.updateRecipe = PersistenceConstants.FLAG_NOT_UPDATE_PICTURE
            this.recipe.timestamp = System.currentTimeMillis()
            this.recipe.author = firebaseUtils.getCurrentUser()?.uid
            this.recipe.minutes = try {
                minutes?.toInt() ?: 0
            } catch (e: NumberFormatException) {
                0
            }
            this.recipe.portions = try {
                portions?.toInt() ?: 0
            } catch (e: NumberFormatException) {
                0
            }
        } ?: RecipeWithInfo().apply {
            firebaseUtils.getFirebaseKeyInAdvance(node = FirebaseConstants.PERSONAL_RECIPES_NODE)?.let { key ->
                this.recipe = Recipe(
                    recipeKey = key,
                    personal = true,
                    name = name,
                    normalizedName = name.normalizedString(),
                    type = type,
                    icon = Recipe.getIconFromType(type),
                    picture = picture,
                    updatePicture = PersistenceConstants.FLAG_NOT_UPDATE_PICTURE,
                    vegetarian = vegetarian,
                    favourite = false,
                    updateRecipe = PersistenceConstants.FLAG_NOT_UPDATE_PICTURE,
                    timestamp = System.currentTimeMillis(),
                    author = firebaseUtils.getCurrentUser()?.uid,
                    minutes = try {
                        minutes?.toInt() ?: 0
                    } catch (e: NumberFormatException) {
                        0
                    },
                    portions = try {
                        portions?.toInt() ?: 0
                    } catch (e: NumberFormatException) {
                        0
                    },
                    tip = null,
                    link = null,
                    edited = false
                )
            }
        })
    }
}
