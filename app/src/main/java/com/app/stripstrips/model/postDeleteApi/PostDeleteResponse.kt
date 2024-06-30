package com.app.stripstrips.model.postDeleteApi

import com.google.gson.annotations.SerializedName

data class PostDeleteResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)
