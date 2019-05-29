package com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation.ingredientsandsteps.ingredients

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.rukiasoft.androidapps.cocinaconroll.R
import com.rukiasoft.androidapps.cocinaconroll.persistence.relations.RecipeWithInfo
import com.rukiasoft.androidapps.cocinaconroll.ui.common.MainActivity
import com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation.NewRecipeParent
import com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation.ingredientsandsteps.EditListExplanationFragment
import com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation.ingredientsandsteps.common.Step2CommonFragment


class Step2IngredientsFragment : Step2CommonFragment(), EditListExplanationFragment.BottomSheetDialogHandler {

    private lateinit var viewModel: Step2IngredientsViewModel
    private var explanationOverlayFragment: EditListExplanationFragment? = null

    override val childPosition: NewRecipeParent.ChildPosition
        get() = NewRecipeParent.ChildPosition.SECOND

    override fun onPause() {
        super.onPause()
        listener.setIngredients(adapter.currentList)
        listener.saveIngredientInBox(binding.editRecipeAddItem.text.toString())
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(Step2IngredientsViewModel::class.java)
        binding.editRecipeItemsTitle.text = resourcesManager.getString(R.string.ingredients)

        if(viewModel.needToShowExplanation()) {
            navigateToOverlay()
        }
    }

    override fun getHint(): String =
        resourcesManager.getString(R.string.ingredients_instructions)

    override fun getListOfItems(recipeWithInfo: RecipeWithInfo): List<String>? =
        recipeWithInfo.ingredients?.map { ingredientWrapper -> ingredientWrapper.ingredient }

    override fun getItemsInEditTextBox(): String =
        listener.getIngredientInBox()

    override fun validateFields(): Boolean {
        (activity as? MainActivity)?.hideKeyboard()
        return true
    }

    private fun navigateToOverlay() {
        fragmentManager?.let { fm ->
            fm.findFragmentByTag(EditListExplanationFragment.TAG)?.let {
                fm.beginTransaction().remove(it).commit()
            }
            explanationOverlayFragment = EditListExplanationFragment.newInstance()
            if (explanationOverlayFragment?.isAdded != true) {
                explanationOverlayFragment?.show(fm, EditListExplanationFragment.TAG)
                explanationOverlayFragment?.setTargetFragment(this, 0)
            }
        }
    }

    override fun dialogShown() {
        explanationOverlayFragment?.dialog?.setOnDismissListener {
            explanationOverlayFragment = null
        }
    }
}

