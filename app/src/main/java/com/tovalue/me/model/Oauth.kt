package com.tovalue.me.model


import com.google.gson.annotations.SerializedName

data class  Oauth(
//    @SerializedName("cookie")
//    val cookie: String,
//    @SerializedName("cookie_name")
//    val cookieName: String,
    val jwt: String,
    @SerializedName("status")
    val status: String,
//    @SerializedName("user")
//    val user: User,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("username")
    val userName: String,
    @SerializedName("life_inventory_id")
    val userInventoryId: Int,
    @SerializedName("error")
    val error: String,
    
    // record validator [@Invitation|@username_exists]
    @SerializedName("inventory_id")
    val inventoryId: Int,
    @SerializedName("inventory_status")
    val inventoryStatus: String,
    @SerializedName("invitation_code")
    val invitationCode: String,
    @SerializedName("registration_status")
    val registrationStatus: String,
    @SerializedName("invited_by")
    val invitedBy: String,
    @SerializedName("inviter_picture")
    val invitedUserPicture: String,
    @SerializedName("inviter_to_value_me")
    val invitedUserTvm: String
)