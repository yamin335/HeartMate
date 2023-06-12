package com.tovalue.me.model.whatworksguide

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class VoteResponse {
    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("error")
    @Expose
    var error: String? = null

    @SerializedName("vote_registered_as")
    @Expose
    var voteRegisteredAs: String? = null
}