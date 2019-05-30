package com.rukiasoft.androidapps.cocinaconroll.persistence.utils

object PersistenceConstants {
    const val FLAG_NOT_UPDATE_RECIPE: Int = 0
    const val FLAG_UPLOAD_RECIPE: Int = 2
    const val FLAG_DELETE_RECIPE: Int = 3
    const val FLAG_NOT_UPDATE_PICTURE: Int = 0
    const val FLAG_DOWNLOAD_PICTURE: Int = 1
    const val FLAG_UPLOAD_PICTURE: Int = 2

    const val TYPE_STARTERS: String = "starter"
    const val TYPE_MAIN: String = "main"
    const val TYPE_DESSERTS: String = "dessert"

    const val DEFAULT_PICTURE_NAME: String = "default_picture"
    const val DATABASE_NAME: String = "cookeo_database"

    const val RECIPES_DIR = "recipes"
    const val FORMAT_DATE_TIME = "yyyy-MM-dd_HH:mm:ss"

}