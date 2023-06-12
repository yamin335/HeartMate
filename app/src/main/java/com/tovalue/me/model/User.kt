package com.tovalue.me.model


import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("avatar")
    val avatar: String? = "",
    @SerializedName("capabilities")
    val capabilities: Capabilities,
    @SerializedName("description")
    val description: String,
    @SerializedName("displayname")
    val displayname: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("firstname")
    val firstname: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("lastname")
    val lastname: String,
    @SerializedName("nicename")
    val nicename: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("registered")
    val registered: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("username")
    val username: String
) {
    @SerializedName("error")
    val error: String = ""
}