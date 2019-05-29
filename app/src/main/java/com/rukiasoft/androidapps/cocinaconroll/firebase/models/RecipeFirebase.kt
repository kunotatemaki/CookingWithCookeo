package com.rukiasoft.androidapps.cocinaconroll.firebase.models

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import com.rukiasoft.androidapps.cocinaconroll.persistence.relations.RecipeWithInfo

@IgnoreExtraProperties
class RecipeFirebase {

    lateinit var name: String
    lateinit var type: String
    var picture: String? = null
    var ingredients: List<String> = listOf()
    var steps: List<String> = listOf()
    var author: String? = null
    var link: String? = null
    var vegetarian: Boolean = false
    var portions: Int = 0
    var minutes: Int = 0
    var tip: String? = null

    companion object {
        fun fromRecipeWithAllInfo(recipeWithInfo: RecipeWithInfo): RecipeFirebase =
            RecipeFirebase().apply {
                name = recipeWithInfo.recipe.name
                type = recipeWithInfo.recipe.type
                picture = recipeWithInfo.recipe.picture
                ingredients = recipeWithInfo.ingredients?.map { it.ingredient } ?: listOf()
                steps = recipeWithInfo.steps?.map { it.step } ?: listOf()
                author = recipeWithInfo.recipe.author
                link = recipeWithInfo.recipe.link
                vegetarian = recipeWithInfo.recipe.vegetarian
                portions = recipeWithInfo.recipe.portions
                minutes = recipeWithInfo.recipe.minutes
                tip = recipeWithInfo.recipe.tip
            }

    }

    @Exclude
    fun toMap(): Map<String, Any?> = mapOf(
        "name" to name,
        "author" to author,
        "link" to link,
        "type" to type,
        "picture" to picture,
        "vegetarian" to vegetarian,
        "portions" to portions,
        "minutes" to minutes,
        "tip" to tip,
        "ingredients" to ingredients,
        "steps" to steps
    )


}
