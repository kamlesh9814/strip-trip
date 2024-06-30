package com.app.stripstrips.model.userListingApi

import com.google.gson.annotations.SerializedName

data class UserListingRequest(

    @field:SerializedName("pageNo")
    val pageNo: String? = null,

    @field:SerializedName("perPage")
    val perPage: String? = null,

    @field:SerializedName("search")
    val search: String? = null

)
