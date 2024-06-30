package com.app.stripstrips.model.tripAndSharedByOtherListingApi

import com.google.gson.annotations.SerializedName

data class TripSharedByOtherListingResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: ArrayList<TripSharedByOtherListingDataItem>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class TripSharedByOtherListingDataItem(

	@field:SerializedName("isDisable")
	val isDisable: String? = null,

	@field:SerializedName("thumbImage")
	val thumbImage: String? = null,

	@field:SerializedName("tripPostIds")
	val tripPostIds: String? = null,

	@field:SerializedName("senderId")
	val senderId: String? = null,

	@field:SerializedName("created")
	val created: String? = null,

	@field:SerializedName("sharedBy")
	val sharedBy: SharedBy? = null,

	@field:SerializedName("tripId")
	val tripId: String? = null,

	@field:SerializedName("tripName")
	val tripName: String? = null,

	@field:SerializedName("tripImage")
	val tripImage: String? = null,

	@field:SerializedName("postId")
	val postId: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)

data class SharedBy(

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
