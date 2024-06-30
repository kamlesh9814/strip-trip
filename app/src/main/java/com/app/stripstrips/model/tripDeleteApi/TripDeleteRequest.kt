package com.app.stripstrips.model.tripDeleteApi

import com.google.gson.annotations.SerializedName

data class TripDeleteRequest(

    @field:SerializedName("tripId")
    val tripId: String? = null,

    @field:SerializedName("type")
    val type: String? = null
)
