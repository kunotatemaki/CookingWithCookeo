package com.rukiasoft.androidapps.cocinaconroll.ui.recipelist

import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import androidx.navigation.Navigator
import javax.inject.Inject

class RecipeListViewModel @Inject constructor(
) : ViewModel() {
    var directions: NavDirections? = null
    var extras: Navigator.Extras? = null

}
