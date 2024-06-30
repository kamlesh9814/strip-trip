package com.app.stripstrips.model.LatlongApiModel

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlacesLatLongModel(

    @field:SerializedName("result")
    val result: Result? = null,

    @field:SerializedName("status")
    val status: String? = null
) : Parcelable

@Parcelize
data class Location(

    @field:SerializedName("lng")
    val lng: Double? = null,

    @field:SerializedName("lat")
    val lat: Double? = null
) : Parcelable

@Parcelize
data class Southwest(

    @field:SerializedName("lng")
    val lng: Double? = null,

    @field:SerializedName("lat")
    val lat: Double? = null
) : Parcelable

@Parcelize
data class Northeast(

    @field:SerializedName("lng")
    val lng: Double? = null,

    @field:SerializedName("lat")
    val lat: Double? = null
) : Parcelable

@Parcelize
data class Geometry(

    @field:SerializedName("viewport")
    val viewport: Viewport? = null,

    @field:SerializedName("location")
    val location: Location? = null
) : Parcelable

@Parcelize
data class Viewport(

    @field:SerializedName("southwest")
    val southwest: Southwest? = null,

    @field:SerializedName("northeast")
    val northeast: Northeast? = null
) : Parcelable

@Parcelize
data class Result(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("geometry")
    val geometry: Geometry? = null
) : Parcelable