package com.app.stripstrips.model.addEditTripDiaryApi

import com.google.gson.annotations.SerializedName

data class AddEditTripDiaryResponse(

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
	val updatedAt: String? = null
)
