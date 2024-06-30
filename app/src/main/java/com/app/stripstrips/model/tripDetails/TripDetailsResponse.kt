package com.app.stripstrips.model.tripDetails

import com.google.gson.annotations.SerializedName

data class TripDetailsResponse(

	@field:SerializedName("tripDetails")
	val tripDetails: TripDetails? = null,

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: ArrayList<DataItem>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class TripDetails(

	@field:SerializedName("isDisable")
	val isDisable: String? = null,

	@field:SerializedName("created")
	val created: String? = null,

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
	val updatedAt: String? = null,

	@field:SerializedName("thumbImage")
	val thumbImage: String? = null
)

data class DataItem(

	@field:SerializedName("distance")
	val distance: String? = null,

	@field:SerializedName("created")
	val created: String? = null,

	@field:SerializedName("latitude")
	val latitude: String? = null,

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

	@field:SerializedName("longitude")
	val longitude: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null,

	@field:SerializedName("tripImage")
	val tripImage: String? = null,

	@field:SerializedName("thumbImage")
	val thumbImage: String? = null,

	@field:SerializedName("videoHeight")
	val videoHeight: String? = null,

	@field:SerializedName("videoWidth")
	val videoWidth: String? = null,

	@field:SerializedName("postDate")
	val postDate: String? = null

)
