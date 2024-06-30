package com.app.stripstrips.model.forgrtPasswordApi

import com.google.gson.annotations.SerializedName

data class ForgetPasswordResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)
