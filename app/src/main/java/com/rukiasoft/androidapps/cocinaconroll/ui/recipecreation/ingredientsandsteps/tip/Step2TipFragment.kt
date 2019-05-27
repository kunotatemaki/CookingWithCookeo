package com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation.ingredientsandsteps.tip

import android.os.Bundle
import android.view.View
import com.rukiasoft.androidapps.cocinaconroll.R
import com.rukiasoft.androidapps.cocinaconroll.persistence.relations.RecipeWithInfo
import com.rukiasoft.androidapps.cocinaconroll.ui.common.MainActivity
import com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation.NewRecipeParent
import com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation.ingredientsandsteps.common.Step2CommonFragment

class Step2TipFragment : Step2CommonFragment() {

    override val childPosition: NewRecipeParent.ChildPosition
        get() = NewRecipeParent.ChildPosition.FOURTH

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.editRecipeItemsTitle.text = resourcesManager.getString(R.string.tip)
        binding.editRecipeAddFab.visibility = View.GONE

    }

    override fun getHint(): String =
        resourcesManager.getString(R.string.tip_instructions)


    override fun getListOfItems(recipeWithInfo: RecipeWithInfo): List<String>? = null

    override fun getItemsInEditTextBox(): String =
        listener.getTip() ?: ""

    override fun validateFields(): Boolean {
        listener.saveTip(binding.editRecipeAddItem.text.toString())
        (activity as? MainActivity)?.hideKeyboard()
        return true
    }

}

