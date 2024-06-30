package com.app.stripstrips.model.shareTripApi

import com.google.gson.annotations.SerializedName

data class ShareTripRequest (

    @field:SerializedName("tripId")
    val tripId: String? = null,

    @field:SerializedName("userId")
    val userId: String? = null

        )