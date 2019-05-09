package com.rukiasoft.androidapps.cocinaconroll.persistence.entities

import androidx.room.*
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
    var name: String,
    @ColumnInfo(name = "normalized_name")
    var normalizedName: String,
    @ColumnInfo(name = "type")
    var type: String,
    @ColumnInfo(name = "icon")
    var icon: Int,
    @ColumnInfo(name = "picture")
    var picture: String,
    @ColumnInfo(name = "vegetarian")
    var vegetarian: Boolean,
    @ColumnInfo(name = "favourite")
    var favourite: Boolean,
    @ColumnInfo(name = "minutes")
    var minutes: Int,
    @ColumnInfo(name = "portions")
    var portions: Int,
    @ColumnInfo(name = "author")
    var author: String?,
    @ColumnInfo(name = "link")
    val link: String?,
    @ColumnInfo(name = "tip")
    val tip: String?,
    @ColumnInfo(name = "personal")
    val personal: Boolean,
    @ColumnInfo(name = "edited")
    val edited: Boolean,
    @ColumnInfo(name = "timestamp")
    var timestamp: Long,
    @ColumnInfo(name = "color_clear")
    val colorClear: Int? = null,
    @ColumnInfo(name = "color_dark")
    val colorDark: Int? = null,
    @ColumnInfo(name = "update_recipe")
    var updateRecipe: Int = PersistenceConstants.FLAG_NOT_UPDATE_RECIPE,
    @ColumnInfo(name = "update_picture")
    var updatePicture: Int = PersistenceConstants.FLAG_NOT_UPDATE_PICTURE

) {
    @Ignore
    var rotated: Boolean = false

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
        minutes = if (recipe.minutes > 0) recipe.minutes else 0,
        portions = if (recipe.portions > 0) recipe.portions else 0,
        tip = recipe.tip,
        link = recipe.link,
        edited = false
    )

    companion object {

        @JvmStatic
        fun getIconFromType(type: String?): Int = when (type) {
            PersistenceConstants.TYPE_DESSERTS -> R.drawable.ic_dessert_18
            PersistenceConstants.TYPE_STARTERS -> R.drawable.ic_starters_18
            PersistenceConstants.TYPE_MAIN -> R.drawable.ic_main_18
            else -> R.drawable.ic_all_18
        }
    }

}
