package com.tovalue.me.model


import com.google.gson.annotations.SerializedName

abstract class Error {
    @SerializedName("error")
    val error: String = ""
    
    @SerializedName("status")
    val status: String = ""
}