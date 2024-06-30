package com.app.stripstrips.model.changepasswordApi

import com.google.gson.annotations.SerializedName

data class ChangePasswordResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)
