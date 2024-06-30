package com.app.stripstrips.model.logoutApi

import com.google.gson.annotations.SerializedName

data class LogoutRequest(

    @field:SerializedName("userID")
    val userID: String? = null
)
