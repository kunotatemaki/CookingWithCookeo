package com.rukiasoft.androidapps.cocinaconroll.ui.recipelist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.rukiasoft.androidapps.cocinaconroll.extensions.switchMap
import com.rukiasoft.androidapps.cocinaconroll.persistence.PersistenceManager
import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Recipe
import com.rukiasoft.androidapps.cocinaconroll.utils.AbsentLiveData
import javax.inject.Inject

class RecipeListViewModel @Inject constructor(
    private val persistenceManager: PersistenceManager
) : ViewModel() {

    private val query: MutableLiveData<Boolean> = MutableLiveData()
    private var listOfRecipes: LiveData<PagedList<Recipe>>

    init {
        query.value = false
        query.observeForever {
            //do nothing
        }

        listOfRecipes = query.switchMap {
            if (it.not()) {
                AbsentLiveData.create()
            } else {
                getMyListOfRecipesFiltered()
            }
        }
    }

    fun getListOfRecipes(): LiveData<PagedList<Recipe>> = listOfRecipes

    private fun getMyListOfRecipesFiltered(): LiveData<PagedList<Recipe>> {

        val query = "select * from recipe" /*DbUtils.getQueryForListOfTasks(userGuid = userGuid,
            startDate = filters.startDate?.time, endDate = filters.endDate?.time, read = filters.read, marked = filters.marked,
            classes = filters.classes, teachers = filters.teachers, todoTab = todoTab)*/



        return persistenceManager.getAllRecipes(SimpleSQLiteQuery(query))

    }

    fun filter(){
        query.value = true
    }
}
