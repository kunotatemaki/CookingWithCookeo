package com.rukiasoft.androidapps.cocinaconroll.extensions

import java.text.Normalizer

fun String.normalizedString(): String {
    val normalized: String = Normalizer.normalize(this, Normalizer.Form.NFD)
    return normalized.replace("[^\\p{ASCII}]".toRegex(), "").trim { it <= ' ' }.toLowerCase()
}
