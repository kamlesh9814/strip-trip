package com.app.stripstrips.model.tripDetails

import com.google.gson.annotations.SerializedName

data class TripDetailsRequest(

    @field:SerializedName("tripId")
    val tripId: String? = null,

    @field:SerializedName("pageNo")
    val pageNo: String? = null,

    @field:SerializedName("perPage")
    val perPage: String? = null

)
