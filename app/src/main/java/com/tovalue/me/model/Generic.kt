package com.tovalue.me.model

import com.google.gson.annotations.SerializedName


data class Generic(val message: String, val status: String) {
    val msg: String = ""
    val deleted: Boolean = false
    val error: String = ""
    @SerializedName("plan_status")
    val planStatus: String = ""
}