package com.tovalue.me.ui.auth.primeriii


data class LifeSpectrum(
    var aspect_max: Int? = 1000,
    var aspect_min: Int? = 0,
    var aspects: Aspects? = null,
    var aspectsOfMyLife: Int? = 1102,
    var audioURL: String? = "",
    var day: String? = "As of aug 2 2022",
    var imageURL: String? = "",
    var registrationLevel: Int? = 1,
    var unityModuleState: Int? = 0
)

data class Aspects(
    var aesthetic: Int = 0,
    var emotional: Int = 0,
    var entertainment: Int = 0,
    var intellectual: Int = 0,
    var professional: Int = 0,
    var sexual: Int = 0,
    var spiritual: Int = 0,
    var village: Int = 0
)