package com.rukiasoft.androidapps.cocinaconroll.persistence.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.rukiasoft.androidapps.cocinaconroll.R
import com.rukiasoft.androidapps.cocinaconroll.extensions.normalizedString
import com.rukiasoft.androidapps.cocinaconroll.firebase.models.RecipeFirebase
import com.rukiasoft.androidapps.cocinaconroll.persistence.utils.PersistenceConstants

@Entity(tableName = "recipe", indices = [(Index(value = arrayOf("recipe_key"), unique = true))])
data class Recipe constructor(

    @PrimaryKey
    @ColumnInfo(name = "recipe_key")
    val recipeKey: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "normalized_name")
    val normalizedName: String,
    @ColumnInfo(name = "type")
    val type: String,
    @ColumnInfo(name = "icon")
    val icon: Int,
    @ColumnInfo(name = "picture")
    val picture: String,
    @ColumnInfo(name = "vegetarian")
    val vegetarian: Boolean,
    @ColumnInfo(name = "favourite")
    val favourite: Boolean,
    @ColumnInfo(name = "minutes")
    val minutes: Int,
    @ColumnInfo(name = "portions")
    val portions: Int,
    @ColumnInfo(name = "author")
    val author: String?,
    @ColumnInfo(name = "link")
    val link: String?,
    @ColumnInfo(name = "tip")
    val tip: String?,
    @ColumnInfo(name = "personal")
    val personal: Boolean,
    @ColumnInfo(name = "edited")
    val edited: Boolean,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long,
    @ColumnInfo(name = "update_recipe")
    val updateRecipe: Int = PersistenceConstants.FLAG_NOT_UPDATE_RECIPE,
    @ColumnInfo(name = "update_picture")
    var updatePicture: Int = PersistenceConstants.FLAG_NOT_UPDATE_PICTURE
) {

    constructor(recipe: RecipeFirebase, key: String, personal: Boolean = false) : this(
        recipeKey = key,
        personal = personal,
        name = recipe.name,
        normalizedName = recipe.name.normalizedString(),
        type = recipe.type,
        icon = getIconFromType(recipe.type),
        picture = recipe.picture ?: PersistenceConstants.DEFAULT_PICTURE_NAME,
        updatePicture = if (recipe.picture == null || recipe.picture == PersistenceConstants.DEFAULT_PICTURE_NAME) {
            PersistenceConstants.FLAG_NOT_UPDATE_PICTURE
        } else {
            PersistenceConstants.FLAG_DOWNLOAD_PICTURE
        },
        vegetarian = recipe.vegetarian,
        favourite = false,
        updateRecipe = PersistenceConstants.FLAG_NOT_UPDATE_PICTURE,
        timestamp = System.currentTimeMillis(),
        author = recipe.author,
        minutes = recipe.minutes,
        portions = recipe.portions,
        tip = recipe.tip,
        link = recipe.link,
        edited = false
    )


    companion object {

        fun getIconFromType(type: String?): Int = when (type) {
            PersistenceConstants.TYPE_DESSERTS -> R.drawable.ic_dessert_18
            PersistenceConstants.TYPE_STARTERS -> R.drawable.ic_starters_18
            PersistenceConstants.TYPE_MAIN -> R.drawable.ic_main_18
            else -> R.drawable.ic_all_18
        }
    }

}
