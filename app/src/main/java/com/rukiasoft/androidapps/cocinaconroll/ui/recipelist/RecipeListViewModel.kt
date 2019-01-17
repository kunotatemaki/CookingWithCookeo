package com.rukiasoft.androidapps.cocinaconroll.ui.recipelist

import androidx.lifecycle.ViewModel
import com.rukiasoft.androidapps.cocinaconroll.persistence.PersistenceManager
import javax.inject.Inject

class RecipeListViewModel @Inject constructor(
    private val persistenceManager: PersistenceManager
) : ViewModel() {

}
