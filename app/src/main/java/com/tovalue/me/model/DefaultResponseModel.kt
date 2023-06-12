package com.tovalue.me.model

import com.google.gson.annotations.SerializedName

data class DefaultApiResponse(
    val data: List<DefaultApiResponseData?>,
    val status: String?
){
    @SerializedName("error")
    val error: String = ""
}

data class DefaultApiResponseData(
    val response: String?
)