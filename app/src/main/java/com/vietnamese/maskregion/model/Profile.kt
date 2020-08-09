package com.vietnamese.maskregion.model

import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.gson.annotations.SerializedName

@IgnoreExtraProperties
data class Profile(
    var displayName: String? = "",
    var description: String? = "",
    var occupation: String? = "",
    var photoUrl: String? = "",
    var contacted: Int? = 0,
    var status: Int = 0,
    var contacted_ids: MutableList<String> = mutableListOf(),
    var warning: Int = 0,

    var username: String? = ""
) {
    companion object {
        const val FIELD_CITY = "displayName"
        const val FIELD_CATEGORY = "discription"
        const val FIELD_PRICE = "username"
    }
}
//class Profile {
//    @SerializedName("displayName")
//     val displayName: String = ""
//    @SerializedName("discription")
//     val discription: String = ""
//    @SerializedName("username")
//     val username: String = ""
//
//}