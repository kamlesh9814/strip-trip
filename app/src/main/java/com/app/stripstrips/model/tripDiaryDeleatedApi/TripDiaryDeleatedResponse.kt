package com.app.stripstrips.model.tripDiaryDeleatedApi

import com.google.gson.annotations.SerializedName

data class TripDiaryDeleatedResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)
