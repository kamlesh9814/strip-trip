package com.app.stripstrips.model.userListingApi

import com.google.gson.annotations.SerializedName

data class UserListingResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: ArrayList<UserListingDataItem>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class UserListingDataItem(

	@field:SerializedName("created")
	val created: String? = null,

	@field:SerializedName("dislikeCount")
	val dislikeCount: String? = null,

	@field:SerializedName("likeCount")
	val likeCount: String? = null,

	@field:SerializedName("userName")
	val userName: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("verificationCode")
	val verificationCode: String? = null,

	@field:SerializedName("emailVerified")
	val emailVerified: String? = null,

	@field:SerializedName("isDisable")
	val isDisable: String? = null,

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("userImage")
	val userImage: String? = null,

	@field:SerializedName("phoneNumber")
	val phoneNumber: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
