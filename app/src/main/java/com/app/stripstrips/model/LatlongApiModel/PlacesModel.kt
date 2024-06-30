package com.app.stripstrips.model.LatlongApiModel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlacesModel(

	@field:SerializedName("predictions")
	val predictions: ArrayList<PredictionsItem?>? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable

@Parcelize
data class StructuredFormatting(
	@field:SerializedName("main_text_matched_substrings")
	val mainTextMatchedSubstrings: List <MainTextMatchedSubstringsItem?>? = null,

	@field:SerializedName("secondary_text")
	val secondaryText: String? = null,

	@field:SerializedName("main_text")
	val mainText: String? = null
) : Parcelable


@Parcelize
data class MatchedSubstringsItem(

	@field:SerializedName("offset")
	val offset: Int? = null,

	@field:SerializedName("length")
	val length: Int? = null
) : Parcelable

@Parcelize
data class PredictionsItem(

	@field:SerializedName("reference")
	val reference: String? = null,

	@field:SerializedName("types")
	val types: List<String?>? = null,

	@field:SerializedName("matched_substrings")
	val matchedSubstrings: List<MatchedSubstringsItem?>? = null,

	@field:SerializedName("terms")
	val terms: List<TermsItem?>? = null,

	@field:SerializedName("structured_formatting")
	val structuredFormatting: StructuredFormatting? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("place_id")
	val placeId: String? = null
) : Parcelable

@Parcelize
data class TermsItem(

	@field:SerializedName("offset")
	val offset: Int? = null,

	@field:SerializedName("value")
	val value: String? = null
) : Parcelable

@Parcelize
data class MainTextMatchedSubstringsItem(

	@field:SerializedName("offset")
	val offset: Int? = null,

	@field:SerializedName("length")
	val length: Int? = null
) : Parcelable
