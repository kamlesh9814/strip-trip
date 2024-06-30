package com.app.stripstrips.model.addPostApi

import android.graphics.Bitmap
import android.net.Uri
import com.google.gson.annotations.SerializedName

data class AddPostRequest(

    @field:SerializedName("postId")
    val postId: String? = null,

    @field:SerializedName("postName")
    val postName: String? = null,

    @field:SerializedName("postDescription")
    val postDescription: String? = null,

    @field:SerializedName("location")
    val location: String? = null,

    @field:SerializedName("diaryId")
    val diaryId: String? = null,

    @field:SerializedName("postImage")
    val postImage: Uri? = null,

    @field:SerializedName("postPrivacy")
    val postPrivacy: String? = null,
    val media_type: String,

    @field:SerializedName("longitude")
    val longitude: Double? = null,

    @field:SerializedName("latitude")
    val latitude: Double? = null,

    @field:SerializedName("videoHeight")
    val videoHeight: Int? = null,

    @field:SerializedName("videoWidth")
    val videoWidth: Int? = null,

    @field:SerializedName("thumbImage")
    val thumbImage: Bitmap?,

    @field:SerializedName("postDate")
    val postDate: String? = null

)

