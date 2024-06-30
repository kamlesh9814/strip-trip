package com.app.stripstrips.model.tripsDiaryListingApi

import com.google.gson.annotations.SerializedName

data class TripsDiaryListingRequest (

    @field:SerializedName("pageNo")
    val pageNo: String? = null,

    @field:SerializedName("perPage")
    val perPage: String? = null
)