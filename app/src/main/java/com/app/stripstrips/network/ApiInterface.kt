package com.app.stripstrips.network

import com.app.stripstrips.model.LatlongApiModel.PlacesLatLongModel
import com.app.stripstrips.model.LatlongApiModel.PlacesModel
import com.app.stripstrips.model.addEditTripDiaryApi.AddEditTripDiaryResponse
import com.app.stripstrips.model.addPostApi.AddPostResponse
import com.app.stripstrips.model.changepasswordApi.ChangePasswordResponse
import com.app.stripstrips.model.editprofileApi.EditProfileResponse
import com.app.stripstrips.model.forgrtPasswordApi.ForgetPasswordResponse
import com.app.stripstrips.model.homeApi.HomeResponse
import com.app.stripstrips.model.likeDislikeApi.LikeDislikeResponse
import com.app.stripstrips.model.loginApi.LoginResponse
import com.app.stripstrips.model.logoutApi.LogoutRequest
import com.app.stripstrips.model.logoutApi.LogoutResponse
import com.app.stripstrips.model.postDeleteApi.PostDeleteResponse
import com.app.stripstrips.model.postDetailsApi.PostDetailsResponse
import com.app.stripstrips.model.shareTripApi.ShareTripResponse
import com.app.stripstrips.model.signupApi.SignUpResponse
import com.app.stripstrips.model.tripAndSharedByOtherListingApi.TripSharedByOtherListingResponse
import com.app.stripstrips.model.tripDeleteApi.TripDeleteResponse
import com.app.stripstrips.model.tripDetails.TripDetailsResponse
import com.app.stripstrips.model.tripDiaryDeleatedApi.TripDiaryDeleatedResponse
import com.app.stripstrips.model.tripsDiaryDetailsApi.TripsDiaryDetailsResponse
import com.app.stripstrips.model.tripsDiaryListingApi.TripsDiaryListingResponse
import com.app.stripstrips.model.userListingApi.UserListingResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {

    // signup Api
    @FormUrlEncoded
    @POST("signUp.php")
    suspend fun sendSignUpData(
        @Field("userName") userName: String,
        @Field("email") email: String,
        @Field("phoneNumber") phoneNumber: String,
        @Field("password") password: String,
    ): Response<SignUpResponse>

    // Login Api
    @FormUrlEncoded
    @POST("logIn.php")
    suspend fun getLoginData(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("deviceType") deviceType: String,
        @Field("deviceToken") deviceToken: String,
    ): Response<LoginResponse>

    // Forget Password Api
    @FormUrlEncoded
    @POST("forgetPassword.php")
    suspend fun getForgetPassword(
        @Field("email") email: String,
    ): Response<ForgetPasswordResponse>

    // Change Password Api
    @FormUrlEncoded
    @POST("changePassword.php")
    suspend fun getChangePassword(
        @Field("password") password: String,
        @Field("newPassword") newPassword: String,
    ): Response<ChangePasswordResponse>

    // Edit Profile Api
    @Multipart
    @POST("editProfile.php")
    suspend fun getEditProfile(
        @Part("userName") userID: RequestBody,
        @Part("email") Name: RequestBody,
        @Part("phoneNumber") email: RequestBody,
        @Part userImage: MultipartBody.Part?,
    ): Response<EditProfileResponse>

    // Add Post Api
    @Multipart
    @POST("addEditPost.php")
    suspend fun getAddPost(
        @Part("postId") postId: RequestBody,
        @Part("postName") postName: RequestBody,
        @Part("postDescription") postDescription: RequestBody,
        @Part("location") location: RequestBody,
        @Part("diaryId") diaryId: RequestBody,
        @Part postImage: MultipartBody.Part?,
        @Part("postPrivacy") postPrivacy: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part("videoHeight") videoHeight: RequestBody,
        @Part("videoWidth") videoWidth: RequestBody,
        @Part("postDate") postDate: RequestBody,
        @Part thumbImage: MultipartBody.Part?
    ): Response<AddPostResponse>

    // Home Api
    @FormUrlEncoded
    @POST("home.php")
    suspend fun homePageApi(
        @Field("pageNo") pageNo: String,
        @Field("perPage") perPage: String,
    ): Response<HomeResponse>

    // Add Edit Trip Diary Api
    @Multipart
    @POST("addEditTripDiary.php")
    suspend fun addEditTripDiary(
        @Part("diaryId") diaryId: RequestBody,
        @Part("tripName") tripName: RequestBody,
        @Part("tripDescreption") tripDescreption: RequestBody,
        @Part tripImage: MultipartBody.Part?,
    ): Response<AddEditTripDiaryResponse>

    // trip Diary Listing Api
    @FormUrlEncoded
    @POST("tripDiaryListing.php")
    suspend fun tripDiaryListing(
        @Field("pageNo") pageNo: String,
        @Field("perPage") perPage: String,
    ): Response<TripsDiaryListingResponse>

    // trip Diary Details Api
    @FormUrlEncoded
    @POST("tripDiaryDetailsV2.php")
    suspend fun tripDiaryDetails(
        @Field("diaryId") diaryId: String,
        @Field("pageNo") pageNo: String,
        @Field("perPage") perPage: String,
    ): Response<TripsDiaryDetailsResponse>

    // trip Shared By  OtherListing Api
    @FormUrlEncoded
    @POST("tripAndSharedByotherTripListing.php")
    suspend fun sharedByOtherTripList(
        @Field("pageNo") pageNo: String,
        @Field("perPage") perPage: String,
        @Field("type") type: String,
        @Field("search") search: String
    ): Response<TripSharedByOtherListingResponse>


    // trip Details Api
    @FormUrlEncoded
    @POST("tripDetails.php")
    suspend fun tripDetails(
        @Field("tripId") tripId: String,
        @Field("pageNo") pageNo: String,
        @Field("perPage") perPage: String,
    ): Response<TripDetailsResponse>

    // post Details Api
    @FormUrlEncoded
    @POST("postDetails.php")
    suspend fun postDetails(
        @Field("postId") postId: String
    ): Response<PostDetailsResponse>

    // Like Dislike Api
    @FormUrlEncoded
    @POST("likeDislikePost.php")
    suspend fun likeDislikeDetails(
        @Field("postId") postId: String,
        @Field("isLike") isLike: String
    ): Response<LikeDislikeResponse>

    // User Listing Api
    @FormUrlEncoded
    @POST("userListing.php")
    suspend fun userListingDetails(
        @Field("pageNo") pageNo: String,
        @Field("perPage") perPage: String,
        @Field("search") search: String
    ): Response<UserListingResponse>

    // Logout Api
    @POST("logout.php")
    suspend fun getLogout(@Body logoutOutResponse: LogoutRequest): Response<LogoutResponse>

    // Share Trip Api
    @FormUrlEncoded
    @POST("shareTrip.php")
    suspend fun shareTripDetails(
        @Field("tripId") tripId: String,
        @Field("userId") userId: String
    ): Response<ShareTripResponse>

    // Trip Diary Deleted Api
    @FormUrlEncoded
    @POST("tripDiaryDeleted.php")
    suspend fun tripDiaryDeleted(
        @Field("diaryId") diaryId: String
    ): Response<TripDiaryDeleatedResponse>


/** Location Api for AddEditPost Screen **/
    @GET
    suspend fun placesAPiSerrvice(
      @Url url: String
    ):Response<PlacesModel>

    @GET
    suspend fun placesLatlongSerrvice(
        @Url url: String
    ):Response<PlacesLatLongModel>

    // Trip Deleted Api
    @FormUrlEncoded
    @POST("tripDelete.php")
    suspend fun tripDeleted(
        @Field("tripId") tripId: String,
        @Field("type") type: String
    ): Response<TripDeleteResponse>

    // Post Deleted Api
    @FormUrlEncoded
    @POST("postDelete.php")
    suspend fun postDeleted(
        @Field("postId") tripId: String
    ): Response<PostDeleteResponse>

}