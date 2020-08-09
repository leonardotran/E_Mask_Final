package com.vietnamese.maskregion.model

import com.google.firebase.firestore.IgnoreExtraProperties


@IgnoreExtraProperties
data class Device(
    var userId: String? = null,
    var deviceMacAddress:String? = null
)
