package com.rukiasoft.androidapps.cocinaconroll.firebase.models

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class TimestampFirebase {

    var timestamp: Long = 0L

    @Exclude
    fun toMap(): Map<String, Any> = mapOf(
        "timestamp" to timestamp
    )
}