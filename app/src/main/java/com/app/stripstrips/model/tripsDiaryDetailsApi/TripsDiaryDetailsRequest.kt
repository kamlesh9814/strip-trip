package com.app.stripstrips.model.tripsDiaryDetailsApi

import com.google.gson.annotations.SerializedName

data class TripsDiaryDetailsRequest(

    @field:SerializedName("diaryId")
    val diaryId: String? = null,

    @field:SerializedName("pageNo")
    val pageNo: String? = null,

    @field:SerializedName("perPage")
    val perPage: String? = null
)