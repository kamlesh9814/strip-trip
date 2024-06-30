package com.app.stripstrips.model.postDetailsApi

import com.google.gson.annotations.SerializedName

data class PostDetailsResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class Data(

	@field:SerializedName("created")
	val created: String? = null,

	@field:SerializedName("latitude")
	val latitude: String? = null,

	@field:SerializedName("isVideo")
	val isVideo: String? = null,

	@field:SerializedName("postId")
	val postId: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("postPrivacy")
	val postPrivacy: String? = null,

	@field:SerializedName("videoHeight")
	val videoHeight: String? = null,

	@field:SerializedName("videoWidth")
	val videoWidth: String? = null,

	@field:SerializedName("isDisable")
	val isDisable: String? = null,

	@field:SerializedName("postImage")
	val postImage: String? = null,

	@field:SerializedName("thumbImage")
	val thumbImage: String? = null,

	@field:SerializedName("diaryId")
	val diaryId: String? = null,

	@field:SerializedName("postName")
	val postName: String? = null,

	@field:SerializedName("postDate")
	val postDate: String? = null,

	@field:SerializedName("location")
	val location: String? = null,

	@field:SerializedName("postDescription")
	val postDescription: String? = null,

	@field:SerializedName("longitude")
	val longitude: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
