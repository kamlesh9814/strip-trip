package com.app.stripstrips.model.likeDislikeApi

import com.google.gson.annotations.SerializedName

data class LikeDislikeRequest(

    @field:SerializedName("postId")
    val postId: String? = null,

    @field:SerializedName("isLike")
    val isLike: String? = null
)
