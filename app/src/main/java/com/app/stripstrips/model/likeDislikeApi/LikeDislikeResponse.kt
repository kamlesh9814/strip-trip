package com.app.stripstrips.model.likeDislikeApi

import com.app.stripstrips.model.tripDetails.TripDetails
import com.google.gson.annotations.SerializedName

data class LikeDislikeResponse(

    @field:SerializedName("code")
    val code: Int? = null,

    @field:SerializedName("isLike")
    val isLike: String? = null,

    @field:SerializedName("data")
    val data: TripDetails? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Int? = null,

    @field:SerializedName("androidTripId")
    val androidTripId: String? = null


)
