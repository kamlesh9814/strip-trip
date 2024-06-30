package com.app.stripstrips.model.homeApi

import androidx.fragment.app.FragmentActivity
import com.google.gson.annotations.SerializedName

data class HomeRequest(
    @field:SerializedName("pageNo")
    val pageNo: String? = null,

    @field:SerializedName("perPage")
    val perPage: String? = null,

)