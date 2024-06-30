package com.app.stripstrips.model.editprofileApi

import android.net.Uri
import com.google.gson.annotations.SerializedName

data class EditProfileRequest(

    @field:SerializedName("userName")
    val userName: String? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("phoneNumber")
    val phoneNumber: String? = null,

    @field:SerializedName("userImage")
    val userImage: Uri?=null

    )