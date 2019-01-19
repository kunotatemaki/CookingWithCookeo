package com.rukiasoft.androidapps.cocinaconroll.persistence.utils

import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
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

class QueryMaker @Inject constructor() {

    fun getQueryForAllRecipes(): SupportSQLiteQuery{
        val query = "${getRecipeBaseQuery()} WHERE update_Recipe <> ${PersistenceConstants.FLAG_DELETE_RECIPE} ${getOrderSuffix()}"
        return SimpleSQLiteQuery(query)
    }

    fun getQueryForVegetarianRecipes(): SupportSQLiteQuery{
        val query = "${getRecipeBaseQuery()} WHERE vegetarian = 1 AND update_Recipe <> ${PersistenceConstants.FLAG_DELETE_RECIPE} ${getOrderSuffix()}"
        return SimpleSQLiteQuery(query)
    }

    fun getQueryForFavouriteRecipes(): SupportSQLiteQuery{
        val query = "${getRecipeBaseQuery()} WHERE favourite = 1 AND update_Recipe <> ${PersistenceConstants.FLAG_DELETE_RECIPE} ${getOrderSuffix()}"
        return SimpleSQLiteQuery(query)
    }

    fun getQueryForStarterRecipes(): SupportSQLiteQuery{
        val query = "${getRecipeBaseQuery()} WHERE type LIKE '${PersistenceConstants.TYPE_STARTERS}' AND update_Recipe <> ${PersistenceConstants.FLAG_DELETE_RECIPE} ${getOrderSuffix()}"
        return SimpleSQLiteQuery(query)
    }

    fun getQueryForMainRecipes(): SupportSQLiteQuery{
        val query = "${getRecipeBaseQuery()} WHERE type LIKE '${PersistenceConstants.TYPE_MAIN}' AND update_Recipe <> ${PersistenceConstants.FLAG_DELETE_RECIPE} ${getOrderSuffix()}"
        return SimpleSQLiteQuery(query)
    }

    fun getQueryForDessertRecipes(): SupportSQLiteQuery{
        val query = "${getRecipeBaseQuery()} WHERE type LIKE '${PersistenceConstants.TYPE_DESSERTS}' AND update_Recipe <> ${PersistenceConstants.FLAG_DELETE_RECIPE} ${getOrderSuffix()}"
        return SimpleSQLiteQuery(query)
    }


    fun getQueryForName(name: String): SupportSQLiteQuery{
        val query = "${getRecipeBaseQuery()} WHERE normalized_name LIKE '%$name%' AND update_Recipe <> ${PersistenceConstants.FLAG_DELETE_RECIPE} ${getOrderSuffix()}"
        return SimpleSQLiteQuery(query)
    }



    private fun getRecipeBaseQuery(): String = "SELECT * FROM recipe"

    private fun getOrderSuffix(): String = "ORDER BY normalized_name ASC"
}