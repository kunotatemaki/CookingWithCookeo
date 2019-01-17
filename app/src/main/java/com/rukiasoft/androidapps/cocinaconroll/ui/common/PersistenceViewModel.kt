package com.rukiasoft.androidapps.cocinaconroll.ui.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.rukiasoft.androidapps.cocinaconroll.extensions.switchMap
import com.rukiasoft.androidapps.cocinaconroll.persistence.PersistenceManager
import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Recipe
import com.rukiasoft.androidapps.cocinaconroll.persistence.utils.QueryMaker
import javax.inject.Inject


/**
 * Copyright (C) Rukiasoft - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Roll <raulfeliz@gmail.com>, January 2019
 *
 *
 */

class PersistenceViewModel @Inject constructor(
    private val persistenceManager: PersistenceManager,
    private val queryMaker: QueryMaker
) : ViewModel() {

    private val query: MutableLiveData<Pair<FilterType, String?>> = MutableLiveData()
    private var listOfRecipes: LiveData<PagedList<Recipe>>

    enum class FilterType {
        ALL,
        STARTER,
        MAIN,
        DESSERT,
        VEGETARIAN,
        FAVOURITE,
        OWN,
        BY_NAME
    }

    init {
        query.value = Pair(FilterType.ALL, null)
        query.observeForever {
            //do nothing
        }

        listOfRecipes = query.switchMap {
            val query = when (it.first) {
                FilterType.STARTER -> queryMaker.getQueryForStarterRecipes()
                FilterType.MAIN -> queryMaker.getQueryForMainRecipes()
                FilterType.DESSERT -> queryMaker.getQueryForDessertRecipes()
                FilterType.VEGETARIAN -> queryMaker.getQueryForVegetarianRecipes()
                FilterType.FAVOURITE -> queryMaker.getQueryForFavouriteRecipes()
                FilterType.OWN -> queryMaker.getQueryForFavouriteRecipes()
                FilterType.ALL -> queryMaker.getQueryForAllRecipes()
                FilterType.BY_NAME -> queryMaker.getQueryForFavouriteRecipes()
            }
            persistenceManager.getRecipes(query)
        }
    }

    fun getListOfRecipes(): LiveData<PagedList<Recipe>> = listOfRecipes

    fun setFilter(filterType: FilterType, text: String? = null){
        query.value = Pair(filterType, text)
    }

}