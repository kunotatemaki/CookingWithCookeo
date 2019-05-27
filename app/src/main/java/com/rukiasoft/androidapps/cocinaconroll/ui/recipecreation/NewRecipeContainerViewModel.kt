package com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rukiasoft.androidapps.cocinaconroll.extensions.normalizedString
import com.rukiasoft.androidapps.cocinaconroll.firebase.FirebaseConstants
import com.rukiasoft.androidapps.cocinaconroll.firebase.FirebaseUtils
import com.rukiasoft.androidapps.cocinaconroll.persistence.PersistenceManager
import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Ingredient
import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Recipe
import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Step
import com.rukiasoft.androidapps.cocinaconroll.persistence.relations.RecipeWithInfo
import com.rukiasoft.androidapps.cocinaconroll.persistence.utils.PersistenceConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewRecipeContainerViewModel @Inject constructor(
    private val firebaseUtils: FirebaseUtils,
    private val persistenceManager: PersistenceManager
) : ViewModel() {
    var selectedPosition: NewRecipeParent.ChildPosition = NewRecipeParent.ChildPosition.FIRST
    private val recipeToEdit: MutableLiveData<RecipeWithInfo> = MutableLiveData()
    var ingredientInBox: String = ""
    var stepInBox: String = ""

    fun getRecipe() = recipeToEdit
    private fun setRecipe(recipeWithInfo: RecipeWithInfo) {
        recipeToEdit.value = recipeWithInfo
    }

    fun getMaxWidth(initialX: Float, initialWidth: Int, finalX: Float, finalWidth: Int, animateToRight: Boolean): Int {
        return (if (animateToRight) {
            finalX + finalWidth - initialX
        } else {
            initialX + initialWidth - finalX
        }).toInt()
    }

    fun setIngredients(ingredients: List<String>) {
        val editedIngredients: MutableList<Ingredient> = mutableListOf()
        getRecipe().value?.let { recipe ->
            val key = recipe.recipe.recipeKey
            ingredients.forEachIndexed { index, text ->
                editedIngredients.add(Ingredient(key, index, text))
            }
            recipe.ingredients = editedIngredients
            setRecipe(recipe)
        }
    }

    fun setSteps(steps: List<String>) {
        val editedSteps: MutableList<Step> = mutableListOf()
        getRecipe().value?.let { recipe ->
            val key = recipe.recipe.recipeKey
            steps.forEachIndexed { index, text ->
                editedSteps.add(Step(key, index, text))
            }
            recipe.steps = editedSteps
            setRecipe(recipe)
        }
    }

    fun setStep1(
        name: String,
        picture: String,
        minutes: String?,
        portions: String?,
        type: String,
        vegetarian: Boolean
    ) {
        val recipe = getRecipe().value
        setRecipe(recipe?.apply {
            this.recipe.name = name
            this.recipe.normalizedName = name.normalizedString()
            this.recipe.type = type
            this.recipe.icon = Recipe.getIconFromType(type)
            this.recipe.picture = picture
            this.recipe.updatePicture = PersistenceConstants.FLAG_NOT_UPDATE_PICTURE
            this.recipe.vegetarian = vegetarian
            this.recipe.updateRecipe = PersistenceConstants.FLAG_NOT_UPDATE_PICTURE
            this.recipe.timestamp = System.currentTimeMillis()
            this.recipe.author = firebaseUtils.getCurrentUser()?.uid
            this.recipe.minutes = try {
                minutes?.toInt() ?: 0
            } catch (e: NumberFormatException) {
                0
            }
            this.recipe.portions = try {
                portions?.toInt() ?: 0
            } catch (e: NumberFormatException) {
                0
            }
        } ?: RecipeWithInfo().apply {
            firebaseUtils.getFirebaseKeyInAdvance(node = FirebaseConstants.PERSONAL_RECIPES_NODE)?.let { key ->
                this.recipe = Recipe(
                    recipeKey = key,
                    personal = true,
                    name = name,
                    normalizedName = name.normalizedString(),
                    type = type,
                    icon = Recipe.getIconFromType(type),
                    picture = picture,
                    updatePicture = PersistenceConstants.FLAG_NOT_UPDATE_PICTURE,
                    vegetarian = vegetarian,
                    favourite = false,
                    updateRecipe = PersistenceConstants.FLAG_NOT_UPDATE_PICTURE,
                    timestamp = System.currentTimeMillis(),
                    author = firebaseUtils.getCurrentUser()?.uid,
                    minutes = try {
                        minutes?.toInt() ?: 0
                    } catch (e: NumberFormatException) {
                        0
                    },
                    portions = try {
                        portions?.toInt() ?: 0
                    } catch (e: NumberFormatException) {
                        0
                    },
                    tip = null,
                    link = null,
                    edited = false
                )
            }
        })
    }

    fun saveRecipe() {
        getRecipe().value?.let { recipeWithAllInfo ->
            viewModelScope.launch(Dispatchers.IO) {
                val oldRecipe = persistenceManager.getRecipe(recipeWithAllInfo.recipe.recipeKey)
                recipeWithAllInfo.recipe.apply {
                    edited = true
                    updateRecipe = PersistenceConstants.FLAG_UPLOAD_RECIPE
                    if(this.picture != oldRecipe?.picture){
                        updatePicture = PersistenceConstants.FLAG_UPLOAD_PICTURE
                    }
                }
                persistenceManager.insertRecipes(listOf(recipeWithAllInfo.recipe))
                persistenceManager.insertIngredients(recipeWithAllInfo.ingredients)
                persistenceManager.insertSteps(recipeWithAllInfo.steps)
            }
        }
    }

}
