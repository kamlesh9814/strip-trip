package com.app.stripstrips.utils

import com.google.gson.annotations.SerializedName

data class StatusMessageModel(

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Int? = null,

    @field:SerializedName("code")
    val code: Int? = null,

    )