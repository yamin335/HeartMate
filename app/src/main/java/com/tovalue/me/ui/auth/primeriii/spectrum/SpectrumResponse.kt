package com.tovalue.me.ui.auth.primeriii.spectrum

data class SpectrumResponse(
	val status: String,
	val spectrum_response: String,
	val category_total: Int,
	val category_title: String,
	val direction: String,
	val category_comment: String,
	val aesthetic_side: Int,
	val entertainment_side: Int,
	val intellectual_side: Int,
	val emotional_side: Int,
	val community_side: Int,
	val professional_side: Int,
	val spiritual_side: Int,
	val sexual_side: Int,
	val running_total: Int,
)

// Spectrum object from [@get_profile] api response
// need to remove when backend will be updated
// reuse [@LifeSpectrum] model
data class ProfileSpectrum(
    val aspects_to_life: Int,
    val audio_file: String,
    val last_updated: String
)