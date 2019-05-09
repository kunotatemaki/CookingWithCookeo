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
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.rukiasoft.androidapps.cocinaconroll.NavGraphDirections
import com.rukiasoft.androidapps.cocinaconroll.R
import com.rukiasoft.androidapps.cocinaconroll.databinding.NewRecipeContainerFragmentBinding
import com.rukiasoft.androidapps.cocinaconroll.extensions.switchMap
import com.rukiasoft.androidapps.cocinaconroll.persistence.relations.RecipeWithInfo
import com.rukiasoft.androidapps.cocinaconroll.ui.common.BaseFragment
import com.rukiasoft.androidapps.cocinaconroll.ui.common.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


@ExperimentalCoroutinesApi
class NewRecipeContainerFragment : BaseFragment(), NewRecipeParent, CoroutineScope by MainScope() {

    private lateinit var binding: NewRecipeContainerFragmentBinding

    private lateinit var viewModel: NewRecipeContainerViewModel

    private val recipeToEdit: MutableLiveData<RecipeWithInfo> = MutableLiveData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.apply {
            val safeArgs = NewRecipeContainerFragmentArgs.fromBundle(this)
            safeArgs.recipeKey?.let{key->
                launch {
                    recipeToEdit.value = persistenceManager.getRecipeWithAllInfo(key)
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
                            com.rukiasoft.androidapps.cocinaconroll.R.id.fragment_new_recipe_container
                        ).navigate(Step2FragmentDirections.actionStep2FragmentToStep3Fragment())
                        com.rukiasoft.androidapps.cocinaconroll.R.id.step3Fragment -> findNavController().navigate(
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

        launch {
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
                    binding.selectedDot.x = if(animateToRight) {
                        initialDot.x
                    }else{
                        initialDot.x + initialDot.width - animatedValue
                    }
                    binding.selectedDot.requestLayout()
                }
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        ValueAnimator.ofInt(maxWidth, finalDot.width).apply {
                            duration = (resourcesManager.getInteger(android.R.integer.config_mediumAnimTime) / 2).toLong()
                            interpolator = AccelerateDecelerateInterpolator()
                            addUpdateListener { animation ->
                                val animatedValue = animation.animatedValue as Int
                                binding.selectedDot.layoutParams.width = animatedValue
                                binding.selectedDot.x = if(animateToRight){
                                    finalDot.x + finalDot.width - animatedValue
                                }else{
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

    override fun getRecipe(): LiveData<RecipeWithInfo> = recipeToEdit


}
