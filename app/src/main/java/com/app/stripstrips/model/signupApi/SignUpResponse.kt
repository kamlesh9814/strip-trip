package com.app.stripstrips.model.signupApi

import com.app.stripstrips.model.editprofileApi.Data
import com.google.gson.annotations.SerializedName

data class SignUpResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("status")
	val status: Int? = null
)
