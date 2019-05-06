package com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation

import androidx.lifecycle.ViewModel
import javax.inject.Inject

class NewRecipeContainerViewModel @Inject constructor(): ViewModel() {
    var selectedPosition: NewRecipeParent.ChildPosition = NewRecipeParent.ChildPosition.FIRST

    fun getMaxWidth(initialX: Float, initialWidth: Int, finalX: Float, finalWidth: Int, animateToRight: Boolean): Int{
        return (if(animateToRight){
            finalX + finalWidth - initialX
        }else{
            initialX + initialWidth - finalX
        }).toInt()
    }



}
