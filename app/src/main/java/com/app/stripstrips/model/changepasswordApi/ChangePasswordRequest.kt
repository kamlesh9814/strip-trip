package com.app.stripstrips.model.changepasswordApi

import com.google.gson.annotations.SerializedName

data class ChangePasswordRequest(

    @field:SerializedName("password")
    val password: String? = null,

    @field:SerializedName("newPassword")
    val newPassword: String? = null
)