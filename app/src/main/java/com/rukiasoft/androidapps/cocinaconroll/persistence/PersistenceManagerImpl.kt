package com.rukiasoft.androidapps.cocinaconroll.persistence

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.sqlite.db.SupportSQLiteQuery
import com.rukiasoft.androidapps.cocinaconroll.persistence.databases.CookeoDatabase
import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Ingredient
import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Recipe
import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Step
import com.rukiasoft.androidapps.cocinaconroll.persistence.relations.RecipeWithInfo
import javax.inject.Inject

class PersistenceManagerImpl @Inject constructor(private val db: CookeoDatabase): PersistenceManager{


    companion object {
        /**
         * A good page size is a value that fills at least a screen worth of content on a large
         * device so the User is unlikely to see a null item.
         * You can play with this constant to observe the paging behavior.
         * <p>
         * It's possible to vary this with list device size, but often unnecessary, unless a user
         * scrolling on a large device is expected to scroll through items more quickly than a small
         * device, such as when the large device uses a grid layout of items.
         */
        private const val PAGE_SIZE = 30


    }

    override fun getRecipe(key: String): Recipe? =
        db.recipeDao().getRecipe(key)

    override fun getRecipeAsObservable(key: String): LiveData<Recipe> =
        db.recipeDao().getRecipeAsObservable(key)


    override fun getAllRecipes(query: SupportSQLiteQuery) =
        LivePagedListBuilder(db.recipeDao().getAllRecipesFromRawQuery(query), PAGE_SIZE)
            .build()

    override fun insertRecipes(recipes: List<Recipe>) {
        db.recipeDao().insert(recipes)
    }

    override fun insertIngredients(ingredients: List<Ingredient>) {
        db.ingredientDao().insert(ingredients)
    }

    override fun insertSteps(steps: List<Step>) {
        db.stepDao().insert(steps)
    }

    override fun deleteIngredients(key: String) {
        db.ingredientDao().deleteIngredients(key)
    }

    override fun deleteSteps(key: String) {
        db.stepDao().deleteSteps(key)
    }

}