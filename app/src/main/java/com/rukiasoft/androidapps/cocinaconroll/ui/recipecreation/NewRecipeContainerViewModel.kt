package com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rukiasoft.androidapps.cocinaconroll.persistence.relations.RecipeWithInfo
import javax.inject.Inject

class NewRecipeContainerViewModel @Inject constructor(): ViewModel() {
    var selectedPosition: NewRecipeParent.ChildPosition = NewRecipeParent.ChildPosition.FIRST
    private val recipeToEdit: MutableLiveData<RecipeWithInfo> = MutableLiveData()
    fun getRecipe() = recipeToEdit
    fun setRecipe(recipeWithInfo: RecipeWithInfo){
        recipeToEdit.value = recipeWithInfo
    }

    fun getMaxWidth(initialX: Float, initialWidth: Int, finalX: Float, finalWidth: Int, animateToRight: Boolean): Int{
        return (if(animateToRight){
            finalX + finalWidth - initialX
        }else{
            initialX + initialWidth - finalX
        }).toInt()
    }



}
