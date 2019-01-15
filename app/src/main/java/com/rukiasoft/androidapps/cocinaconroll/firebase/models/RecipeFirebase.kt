package com.rukiasoft.androidapps.cocinaconroll.firebase.models

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class RecipeFirebase {

    var name: String? = null
    var type: String? = null
    var picture: String? = null
    var ingredients: List<String>? = null
    var steps: List<String>? = null
    var author: String? = null
    var link: String? = null
    var vegetarian: Boolean? = null
    var portions: Int? = null
    var minutes: Int? = null
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
