package com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation.ingredientsandsteps

import androidx.lifecycle.ViewModel
import com.rukiasoft.androidapps.cocinaconroll.preferences.PreferencesConstants
import com.rukiasoft.androidapps.cocinaconroll.preferences.PreferencesManager
import javax.inject.Inject

class EditListExplanationViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {
    fun saveExplanationShown() {
        preferencesManager.setBooleanIntoPreferences(PreferencesConstants.PREFERENCE_EXPLANATION, true)
    }
}