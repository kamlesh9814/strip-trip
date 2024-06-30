package com.app.stripstrips.model.homeApi

import com.google.gson.annotations.SerializedName

data class HomeResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: ArrayList<HomeDataItem>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class HomeDataItem(

	@field:SerializedName("langitude")
	val langitude: String? = null,

	@field:SerializedName("created")
	val created: String? = null,

	@field:SerializedName("latitude")
	val latitude: String? = null,

	@field:SerializedName("tripId")
	val tripId: String? = null,

	@field:SerializedName("postId")
	val postId: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("postPrivacy")
	val postPrivacy: String? = null,

	@field:SerializedName("isDisable")
	val isDisable: String? = null,

	@field:SerializedName("postImage")
	val postImage: String? = null,

	@field:SerializedName("diaryId")
	val diaryId: String? = null,

	@field:SerializedName("postName")
	val postName: String? = null,

	@field:SerializedName("location")
	val location: String? = null,

	@field:SerializedName("postDescription")
	val postDescription: String? = null,

	@field:SerializedName("videoHeight")
	val videoHeight: String? = null,

	@field:SerializedName("videoWidth")
	val videoWidth: String? = null,

	@field:SerializedName("thumbImage")
	val thumbImage: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
