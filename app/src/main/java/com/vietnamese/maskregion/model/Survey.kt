package com.vietnamese.maskregion.model

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Survey(
    var answer: MutableList<String>? = null,
    var content:MutableList<String>?  = null
)
