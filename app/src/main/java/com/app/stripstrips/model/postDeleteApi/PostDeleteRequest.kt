package com.app.stripstrips.model.postDeleteApi

import com.google.gson.annotations.SerializedName

data class PostDeleteRequest(

    @field:SerializedName("postId")
    val postId: String? = null

)