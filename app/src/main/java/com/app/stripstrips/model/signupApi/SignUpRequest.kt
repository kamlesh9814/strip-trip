package com.app.stripstrips.model.signupApi

import com.google.gson.annotations.SerializedName

data class SignUpRequest(

    @field:SerializedName("userName")
    val userName: String? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("phoneNumber")
    val phoneNumber: String? = null,

    @field:SerializedName("email")
    val password: String? = null

    )


