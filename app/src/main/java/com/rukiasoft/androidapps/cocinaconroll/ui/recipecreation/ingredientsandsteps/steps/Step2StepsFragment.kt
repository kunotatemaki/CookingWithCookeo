package com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation.ingredientsandsteps.steps

import android.os.Bundle
import com.rukiasoft.androidapps.cocinaconroll.R
import com.rukiasoft.androidapps.cocinaconroll.persistence.relations.RecipeWithInfo
import com.rukiasoft.androidapps.cocinaconroll.ui.common.MainActivity
import com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation.NewRecipeParent
import com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation.ingredientsandsteps.common.Step2CommonFragment

class Step2StepsFragment : Step2CommonFragment() {

    override val childPosition: NewRecipeParent.ChildPosition
        get() = NewRecipeParent.ChildPosition.THIRD

    override fun onPause() {
        super.onPause()
        listener.setSteps(adapter.currentList)
        listener.saveStepInBox(binding.editRecipeAddItem.text.toString())
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.editRecipeItemsTitle.text = resourcesManager.getString(R.string.steps)

    }

    override fun getHint(): String =
        resourcesManager.getString(R.string.steps_instructions)


    override fun getListOfItems(recipeWithInfo: RecipeWithInfo): List<String>? =
        recipeWithInfo.steps?.map { stepWrapper -> stepWrapper.step }

    override fun getItemsInEditTextBox(): String =
        listener.getStepInBox()

    override fun validateFields(): Boolean {
        (activity as? MainActivity)?.hideKeyboard()
        return true
    }

}

