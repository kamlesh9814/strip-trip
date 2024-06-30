package com.app.stripstrips.model.addEditTripDiaryApi

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody

data class AddEditTripsDiaryRequest(

    @field:SerializedName("diaryId")
    val diaryId: String? = null,

    @field:SerializedName("tripName")
    val tripName: String? = null,

    @field:SerializedName("tripDescreption")
    val tripDescreption: String? = null,

    @field:SerializedName("tripImage")
    val tripImage: MultipartBody.Part?

)
