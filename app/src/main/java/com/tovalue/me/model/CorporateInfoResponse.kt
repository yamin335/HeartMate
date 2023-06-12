package com.tovalue.me.model

import com.google.gson.annotations.SerializedName

data class CorporateInfoResponse(
    var cookies: String,
    @SerializedName("help_center")
    var helpCenter: String,
    var licenses: String,
    @SerializedName("member_principles")
    var memberPrinciples: String,
    @SerializedName("non_exclusive_dating_partners")
    var nonExclusiveDatingPartners: String,
    @SerializedName("number_changes")
    var numberChanges: String,
    @SerializedName("onboarding_video")
    var onboardingVideo: String,
    @SerializedName("privacy_policy")
    var privacyPolicy: String,
    @SerializedName("safe_dating_tips")
    var safeDatingTips: String,
    var status: String,
    @SerializedName("terms_of_service")
    var termsOfService: String,
    @SerializedName("spectrum_demo")
    val spectrumDemo: String,
    @SerializedName("are_match_and_dating")
    val areMatchAndDating: String

) {
    @SerializedName("error")
    val error: String = ""
}