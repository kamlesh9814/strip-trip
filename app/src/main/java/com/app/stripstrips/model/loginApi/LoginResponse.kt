package com.app.stripstrips.model.loginApi

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("userData")
	val userData: UserData? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class UserData(

	@field:SerializedName("deviceType")
	val deviceType: String? = null,

	@field:SerializedName("tokenId")
	val tokenId: String? = null,

	@field:SerializedName("created")
	val created: String? = null,

	@field:SerializedName("userName")
	val userName: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("deviceToken")
	val deviceToken: String? = null,

	@field:SerializedName("verificationCode")
	val verificationCode: String? = null,

	@field:SerializedName("userToken")
	val userToken: String? = null,

	@field:SerializedName("emailVerified")
	val emailVerified: String? = null,

	@field:SerializedName("isDisable")
	val isDisable: String? = null,

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("loginTime")
	val loginTime: String? = null,

	@field:SerializedName("userImage")
	val userImage: String? = null,

	@field:SerializedName("phoneNumber")
	val phoneNumber: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
