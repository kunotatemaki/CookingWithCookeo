package com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation.ingredientsandsteps.ingredients

import androidx.lifecycle.ViewModel
import com.rukiasoft.androidapps.cocinaconroll.preferences.PreferencesConstants
import com.rukiasoft.androidapps.cocinaconroll.preferences.PreferencesManager
import javax.inject.Inject

class Step2IngredientsViewModel @Inject constructor(private val preferencesManager: PreferencesManager) : ViewModel() {

    fun needToShowExplanation() =
        preferencesManager.getBooleanFromPreferences(PreferencesConstants.PREFERENCE_EXPLANATION).not()

}