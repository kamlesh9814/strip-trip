package com.app.stripstrips.model.shareTripApi

import com.google.gson.annotations.SerializedName

data class ShareTripResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)
