package com.rukiasoft.androidapps.cocinaconroll.utils

import android.content.Context
import android.graphics.Color
import com.rukiasoft.androidapps.cocinaconroll.resources.ResourcesManager
import javax.inject.Inject

class ViewUtils @Inject constructor(
    private val context: Context,
    private val resourceManager: ResourcesManager
) {

    fun needToSetStatusBarThemeAsDark(color: Int): Boolean {

        var red = Color.red(color) / 255.0
        red = if (red < 0.03928) red / 12.92 else Math.pow((red + 0.055) / 1.055, 2.4)
        var green = Color.green(color) / 255.0
        green = if (green < 0.03928) green / 12.92 else Math.pow((green + 0.055) / 1.055, 2.4)
        var blue = Color.blue(color) / 255.0
        blue = if (blue < 0.03928) blue / 12.92 else Math.pow((blue + 0.055) / 1.055, 2.4)
        return (0.2126 * red + 0.7152 * green + 0.0722 * blue).toFloat() < 0.5
    }

}