package com.tovalue.me.ui.auth.primeriii

import com.google.gson.annotations.SerializedName

data class InventorySide(
	val status: String,
	@SerializedName("categories")
	val sideCategory: List<SideCategory>,
	@SerializedName("questions")
	val questions: List<Questionaire>
)
