package com.rukiasoft.androidapps.cocinaconroll.ui.custom

import kotlin.math.max
import kotlin.math.min


/**
 * Copyright (C) Rukiasoft - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Roll <raulfeliz@gmail.com>, March 2019
 *
 *
 */

object CustomViewsUtils {
    fun mapValueFromRangeToRange(
        value: Double,
        fromLow: Double,
        fromHigh: Double,
        toLow: Double,
        toHigh: Double
    ): Double {
        return toLow + (value - fromLow) / (fromHigh - fromLow) * (toHigh - toLow)
    }

    fun clamp(value: Double, low: Double, high: Double): Double {
        return min(max(value, low), high)
    }
}