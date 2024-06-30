package com.app.stripstrips.model.loginApi

import com.google.gson.annotations.SerializedName

data class LoginRequest(

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("password")
    val password: String? = null,

    @field:SerializedName("deviceType")
    val deviceType: String? = null,

    @field:SerializedName("deviceToken")
    val deviceToken: String? = null

)
