package com.rukiasoft.androidapps.cocinaconroll.firebase.models

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class RecipeFirebase {

    lateinit var name: String
    lateinit var type: String
    var picture: String? = null
    lateinit var ingredients: List<String>
    lateinit var steps: List<String>
    lateinit var author: String
    lateinit var link: String
    var vegetarian: Boolean = false
    var portions: Int = 0
    var minutes: Int = 0
    lateinit var tip: String

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
