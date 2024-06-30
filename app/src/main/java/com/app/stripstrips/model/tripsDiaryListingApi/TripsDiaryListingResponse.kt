package com.app.stripstrips.model.tripsDiaryListingApi

import com.google.gson.annotations.SerializedName

data class TripsDiaryListingResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: ArrayList<TripsDiaryDataItem>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class TripsDiaryDataItem(

	@field:SerializedName("isDisable")
	val isDisable: String? = null,

	@field:SerializedName("diaryId")
	val diaryId: String? = null,

	@field:SerializedName("created")
	val created: String? = null,

	@field:SerializedName("tripName")
	val tripName: String? = null,

	@field:SerializedName("tripDescreption")
	val tripDescreption: String? = null,

	@field:SerializedName("tripImage")
	val tripImage: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null,

	@field:SerializedName("totalPostCount")
	val totalPostCount: String? = null
)
