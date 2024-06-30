package com.app.stripstrips.model.tripAndSharedByOtherListingApi

import androidx.fragment.app.FragmentActivity
import com.google.gson.annotations.SerializedName

data class TripSharedByOtherListingRequest(

    @field:SerializedName("pageNo")
    val pageNo: String? = null,

    @field:SerializedName("perPage")
    val perPage: String? = null,

    @field:SerializedName("type")
    val type: String? = null,

    @field:SerializedName("search")
    val search: String? = null,

)
