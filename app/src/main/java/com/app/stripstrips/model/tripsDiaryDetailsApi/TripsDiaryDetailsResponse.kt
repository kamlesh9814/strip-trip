package com.app.stripstrips.model.tripsDiaryDetailsApi

data class TripsDiaryDetailsResponse(
    val code: Int,
    val `data`: ArrayList<TripsDiaryListingDataItem>,
    val diaryDetails: DiaryDetails,
    val message: String,
    val status: Int
)

data class TripsDiaryListingDataItem(
    val `data`: ArrayList<TripsData>,
    val date: String
)

data class DiaryDetails(
    val created: String,
    val diaryId: String,
    val isDisable: String,
    val tripDescreption: String,
    val tripImage: String,
    val tripName: String,
    val updatedAt: String,
    val userId: String
)

data class TripsData(
    val created: String,
    val diaryId: String,
    val isDisable: String,
    val isVideo: String,
    val latitude: String,
    val location: String,
    val longitude: String,
    val postDate: String,
    val postDescription: String,
    val postId: String,
    val postImage: String,
    val postName: String,
    val postPrivacy: String,
    val thumbImage: String,
    val updatedAt: String,
    val userId: String,
    val videoHeight: String,
    val videoWidth: String
)
