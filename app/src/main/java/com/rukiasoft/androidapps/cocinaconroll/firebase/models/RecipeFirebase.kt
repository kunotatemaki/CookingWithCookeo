package com.rukiasoft.androidapps.cocinaconroll.firebase.models

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

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
