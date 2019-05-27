package com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation.ingredientsandsteps.ingredients

import android.os.Bundle
import com.rukiasoft.androidapps.cocinaconroll.R
import com.rukiasoft.androidapps.cocinaconroll.persistence.relations.RecipeWithInfo
import com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation.NewRecipeParent
import com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation.ingredientsandsteps.common.Step2CommonFragment


class Step2IngredientsFragment : Step2CommonFragment() {

    override val childPosition: NewRecipeParent.ChildPosition
        get() = NewRecipeParent.ChildPosition.SECOND

    override fun onPause() {
        super.onPause()
        listener.setIngredients(adapter.currentList)
        listener.saveIngredientInBox(binding.editRecipeAddItem.text.toString())
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.editRecipeItemsTitle.text = resourcesManager.getString(R.string.ingredients)

    }

    override fun getListOfItems(recipeWithInfo: RecipeWithInfo): List<String>? =
        recipeWithInfo.ingredients?.map { ingredientWrapper -> ingredientWrapper.ingredient }

    override fun getItemsInEditTextBox(): String =
        listener.getIngredientInBox()

    override fun validateFields(): Boolean {
        return true
    }

}
