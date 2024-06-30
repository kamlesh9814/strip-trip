package com.app.stripstrips.network

import android.content.Context
import android.util.Log
import com.app.stripstrips.model.LatlongApiModel.PlacesLatLongModel
import com.app.stripstrips.model.LatlongApiModel.PlacesModel
import com.app.stripstrips.model.addEditTripDiaryApi.AddEditTripDiaryResponse
import com.app.stripstrips.model.addEditTripDiaryApi.AddEditTripsDiaryRequest
import com.app.stripstrips.model.addPostApi.AddPostRequest
import com.app.stripstrips.model.addPostApi.AddPostResponse
import com.app.stripstrips.model.changepasswordApi.ChangePasswordResponse
import com.app.stripstrips.model.editprofileApi.EditProfileRequest
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
import com.app.stripstrips.utils.ConstantsVar
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import retrofit2.http.Url
import java.io.*


class Repository {

    /** code to convert image Uri to ByteArray **/
    @Throws(IOException::class)
    fun getBytes(inputStream: InputStream): ByteArray? {
        val byteBuffer = ByteArrayOutputStream()
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)
        var len = 0
        while (inputStream.read(buffer).also { len = it } != -1) {
            byteBuffer.write(buffer, 0, len)
        }
        return byteBuffer.toByteArray()
    }

    open fun getAlphaNumericString(): String? {
        val n = 20
        val AlphaNumericString = ("ABCDEFGHIJKLMNOPQRSTUVWXYZ" /*+ "0123456789"*/
                + "abcdefghijklmnopqrstuvxyz")
        // create StringBuffer size of AlphaNumericString
        val sb = StringBuilder(n)
        for (i in 0 until n) {
            val index = (AlphaNumericString.length
                    * Math.random()).toInt()
            sb.append(
                AlphaNumericString[index]
            )
        }
        return sb.toString()
    }

    /** signup Api **/
    suspend fun signUpUser(
        userName: String?,
        email: String?,
        phoneNumber: String?,
        password: String?,
    ): Response<SignUpResponse>? {
        return RetrofitClient().apiInterface.sendSignUpData(
            userName!!,
            email!!,
            phoneNumber!!,
            password!!
        )
    }

    /**  Login Api **/
    suspend fun loginInUser(
        email: String?,
        password: String?,
        deviceType: String?,
        deviceToken: String?,
    ): Response<LoginResponse>? {
        return RetrofitClient().apiInterface.getLoginData(
            email!!,
            password!!, deviceType!!, deviceToken!!
        )
    }

    /**  ForgetPassword Api **/
    suspend fun forgetUser(
        email: String?,
    ): Response<ForgetPasswordResponse>? {
        return RetrofitClient().apiInterface.getForgetPassword(email!!)
    }

    /** change Password Api **/
    suspend fun changePasswordUser(
        password: String?, newPassword: String?,
    ): Response<ChangePasswordResponse>? {
        return RetrofitClient().apiInterface.getChangePassword(password!!, newPassword!!)
    }

    /**  Edit Profile Api **/
    suspend fun editProfileUser(
        body: EditProfileRequest,
        context: Context
    ): Response<EditProfileResponse>? {

        val userName = body.userName.toString().toRequestBody("plain/text".toMediaTypeOrNull())
        val email = body.email.toString().toRequestBody("plain/text".toMediaTypeOrNull())
        val phoneNumber =
            body.phoneNumber.toString().toRequestBody("plain/text".toMediaTypeOrNull())
        val user_profile_image: MultipartBody.Part?
        val vFileName = ""
        val vFileBody = RequestBody.create("text/plain".toMediaTypeOrNull(), "")

        if (body.userImage != null) {
            val iStream: InputStream = context.contentResolver.openInputStream(body.userImage!!)!!
            val inputData: ByteArray = getBytes(iStream)!!

            val profileImage: RequestBody? =
                inputData.let {
                    RequestBody.create(
                        "multipart/form-data".toMediaTypeOrNull(), it
                    )
                }
            user_profile_image = profileImage?.let {
                MultipartBody.Part.createFormData(
                    "userImage",
                    getAlphaNumericString()!!.toString() + ".jpg",
                    it
                )
            }
        } else {
            user_profile_image =
                MultipartBody.Part.createFormData(
                    "userImage",
                    vFileName,
                    vFileBody
                )
        }

        return RetrofitClient().apiInterface.getEditProfile(
            userName,
            email,
            phoneNumber,
            user_profile_image
        )
    }

    /** Add Edit Post Api **/
    suspend fun addPostUser(context: Context, body: AddPostRequest): Response<AddPostResponse> {
        Log.e(
            "TAGR",
            "onClick: ${body.videoHeight} ${body.videoWidth} ${body.thumbImage} ${body.postImage} ${body.latitude} ${body.longitude} "
        )
        val postId = body.postId.toString().toRequestBody("plain/text".toMediaTypeOrNull())
        val postName = body.postName.toString().toRequestBody("plain/text".toMediaTypeOrNull())
        val postDescription =
            body.postDescription.toString().toRequestBody("plain/text".toMediaTypeOrNull())
        val location = body.location.toString().toRequestBody("plain/text".toMediaTypeOrNull())
        val diaryId = body.diaryId.toString().toRequestBody("plain/text".toMediaTypeOrNull())
        val longitude = body.latitude.toString().toRequestBody("plain/text".toMediaTypeOrNull())
        val latitude = body.longitude.toString().toRequestBody("plain/text".toMediaTypeOrNull())
        val dateTime = body.postDate.toString().toRequestBody("plain/text".toMediaTypeOrNull())
        val videoHeight =
            body.videoHeight.toString().toRequestBody("plain/text".toMediaTypeOrNull())
        val videoWidth = body.videoWidth.toString().toRequestBody("plain/text".toMediaTypeOrNull())

        var thumbImage: MultipartBody.Part? = null
        var postImage: MultipartBody.Part? = null

        if (body.thumbImage != null) {
            val iStream2: InputStream = context.contentResolver.openInputStream(
                ConstantsVar.getImageUri(
                    context,
                    body.thumbImage!!
                )
            )!!
            val inputData2: ByteArray = getBytes(iStream2)!!

            val paths = ConstantsVar.getFilePath(
                context,
                ConstantsVar.getImageUri(context, body.thumbImage!!)
            )
            val selectedFiles = File(paths)
            val thumb_Image: RequestBody? =
                inputData2.let {
                    RequestBody.create(
                        "multipart/form-data".toMediaTypeOrNull(), it
                    )
                }
            thumbImage = thumb_Image?.let {
                MultipartBody.Part.createFormData(
                    "thumbImage",
                    selectedFiles.name,
                    it
                )
            }
            /** post Image and video **/
            val iStream1: InputStream = context.contentResolver.openInputStream(body.postImage!!)!!
            val inputData1: ByteArray = getBytes(iStream1)!!

            val path = ConstantsVar.getFilePath(context, body.postImage)
            val selectedFile = File(path)
            val profileImage: RequestBody? =
                inputData1.let {
                    RequestBody.create(
                        "multipart/form-data".toMediaTypeOrNull(), it
                    )
                }
            postImage = profileImage?.let {
                MultipartBody.Part.createFormData(
                    "postImage",
                    selectedFile.name,
                    it
                )
            }

        } else {
            /** post Image **/
            val iStream1: InputStream = context.contentResolver.openInputStream(body.postImage!!)!!
            val inputData1: ByteArray = getBytes(iStream1)!!

            val path = ConstantsVar.getFilePath(context, body.postImage)
            val selectedFile = File(path)
            val profileImage: RequestBody? =
                inputData1.let {
                    RequestBody.create(
                        "multipart/form-data".toMediaTypeOrNull(), it
                    )
                }
            postImage = profileImage?.let {
                MultipartBody.Part.createFormData(
                    "postImage",
                    selectedFile.name,
                    it
                )

            }
        }

        var postPrivacy: RequestBody? = null
        postPrivacy = body.postPrivacy?.toRequestBody("plain/text".toMediaTypeOrNull())

        return RetrofitClient().apiInterface.getAddPost(
            postId,
            postName,
            postDescription,
            location,
            diaryId,
            postImage,
            postPrivacy!!,
            longitude,
            latitude,
            videoHeight,
            videoWidth,
            dateTime,
            thumbImage
        )
    }

    /** Update Post Api **/
    suspend fun updatePostUser(context: Context, body: AddPostRequest): Response<AddPostResponse> {
        Log.e(
            "TAGR",
            "onClick: ${body.videoHeight} ${body.videoWidth} ${body.thumbImage} ${body.postImage} ${body.latitude} ${body.longitude} "
        )
        val postId = body.postId.toString().toRequestBody("plain/text".toMediaTypeOrNull())
        val postName = body.postName.toString().toRequestBody("plain/text".toMediaTypeOrNull())
        val postDescription =
            body.postDescription.toString().toRequestBody("plain/text".toMediaTypeOrNull())
        val location = body.location.toString().toRequestBody("plain/text".toMediaTypeOrNull())
        val diaryId = body.diaryId.toString().toRequestBody("plain/text".toMediaTypeOrNull())
        val longitude = body.latitude.toString().toRequestBody("plain/text".toMediaTypeOrNull())
        val latitude = body.longitude.toString().toRequestBody("plain/text".toMediaTypeOrNull())
        val dateTime = body.postDate.toString().toRequestBody("plain/text".toMediaTypeOrNull())
        val videoHeight =
            body.videoHeight.toString().toRequestBody("plain/text".toMediaTypeOrNull())
        val videoWidth = body.videoWidth.toString().toRequestBody("plain/text".toMediaTypeOrNull())

        var thumbImage: MultipartBody.Part? = null
        var postImage: MultipartBody.Part? = null
        if (body.thumbImage != null) {
            val iStream2: InputStream = context.contentResolver.openInputStream(
                ConstantsVar.getImageUri(
                    context,
                    body.thumbImage
                )
            )!!
            val inputData2: ByteArray = getBytes(iStream2)!!

            val paths = ConstantsVar.getFilePath(
                context,
                ConstantsVar.getImageUri(context, body.thumbImage)
            )
            val selectedFiles = File(paths)
            val thumb_Image: RequestBody =
                inputData2.let {
                    RequestBody.create(
                        "multipart/form-data".toMediaTypeOrNull(), it
                    )
                }
            thumbImage = thumb_Image.let {
                MultipartBody.Part.createFormData(
                    "thumbImage",
                    selectedFiles.name,
                    it
                )
            }
            /** post Image and video **/
            val iStream1: InputStream = context.contentResolver.openInputStream(body.postImage!!)!!
            val inputData1: ByteArray = getBytes(iStream1)!!

            val path = ConstantsVar.getFilePath(context, body.postImage)
            val selectedFile = File(path)
            val profileImage: RequestBody? =
                inputData1.let {
                    RequestBody.create(
                        "multipart/form-data".toMediaTypeOrNull(), it
                    )
                }
            postImage = profileImage?.let {
                MultipartBody.Part.createFormData(
                    "postImage",
                    selectedFile.name,
                    it
                )
            }
        } else if (body.postImage != null) {
            /** post Image **/
            val iStream1: InputStream = context.contentResolver.openInputStream(body.postImage)!!
            val inputData1: ByteArray = getBytes(iStream1)!!

            val path = ConstantsVar.getFilePath(context, body.postImage)
            val selectedFile = File(path)
            val profileImage: RequestBody =
                inputData1.let {
                    RequestBody.create(
                        "multipart/form-data".toMediaTypeOrNull(), it
                    )
                }
            postImage = profileImage.let {
                MultipartBody.Part.createFormData(
                    "postImage",
                    selectedFile.name,
                    it
                )

            }
        } else {
            val userPostImage: MultipartBody.Part?

            if (body.postImage != null) {
                val iStream: InputStream =
                    context.contentResolver.openInputStream(body.postImage)!!
                val inputData: ByteArray = getBytes(iStream)!!
                val requestFile1: RequestBody =
                    inputData.let {
                        RequestBody.create(
                            "multipart/form-data".toMediaTypeOrNull(),
                            it
                        )
                    }

                userPostImage = requestFile1.let {
                    MultipartBody.Part.createFormData(
                        "postImage",
                        System.currentTimeMillis().toString() + ".jpg",
                        it
                    )
                }
            }
        }

        var postPrivacy: RequestBody? = null
        postPrivacy = body.postPrivacy?.toRequestBody("plain/text".toMediaTypeOrNull())

        return RetrofitClient().apiInterface.getAddPost(
            postId,
            postName,
            postDescription,
            location,
            diaryId,
            postImage,
            postPrivacy!!,
            longitude,
            latitude,
            videoHeight,
            videoWidth,
            dateTime,
            thumbImage
        )
    }

    /** Home Api **/
    suspend fun homePageAllParams(
        pageNo: String?,
        perPage: String?,
    ): Response<HomeResponse>? {
        return RetrofitClient().apiInterface.homePageApi(pageNo!!, perPage!!)
    }

    /** addEdit Trip Diary Api **/
    suspend fun addEditTripDiaryDetails(
        context: Context,
        body: AddEditTripsDiaryRequest
    ): Response<AddEditTripDiaryResponse>? {
        val diaryId = body.diaryId.toString().toRequestBody("plain/text".toMediaTypeOrNull())
        val tripName = body.tripName.toString().toRequestBody("plain/text".toMediaTypeOrNull())
        val tripDescreption =
            body.tripDescreption.toString().toRequestBody("plain/text".toMediaTypeOrNull())
        val trip_Images = body.tripImage
        return RetrofitClient().apiInterface.addEditTripDiary(
            diaryId,
            tripName,
            tripDescreption,
            trip_Images
        )
    }

    /** trip Diary Listing Api **/
    suspend fun tripDiaryListingApi(
        pageNo: String?,
        perPage: String?
    ): Response<TripsDiaryListingResponse>? {
        return RetrofitClient().apiInterface.tripDiaryListing(pageNo!!, perPage!!)
    }

    /** trip Diary Details Api **/
    suspend fun tripDiaryDetailsApi(
        diaryId: String?,
        pageNo: String?,
        perPage: String?
    ): Response<TripsDiaryDetailsResponse> {
        return RetrofitClient().apiInterface.tripDiaryDetails(diaryId!!, pageNo!!, perPage!!)
    }

    /** Trips Shared By Other Listing Api **/
    suspend fun sharedByOtherTripListApi(
        pageNo: String?,
        perPage: String?,
        type: String?,
        search: String?
    ): Response<TripSharedByOtherListingResponse>? {
        return RetrofitClient().apiInterface.sharedByOtherTripList(
            pageNo!!,
            perPage!!,
            type!!,
            search!!
        )
    }


    /**  trip Details Api **/
    suspend fun tripDetailsApi(
        tripId: String?,
        pageNo: String?,
        perPage: String?
    ): Response<TripDetailsResponse>? {
        return RetrofitClient().apiInterface.tripDetails(tripId!!, pageNo!!, perPage!!)
    }

    /** post Details Api **/
    suspend fun postDetailsApi(
        postId: String?
    ): Response<PostDetailsResponse>? {
        return RetrofitClient().apiInterface.postDetails(postId!!)
    }

    /** Like Dislike Api **/
    suspend fun likeDislikeDetailsApi(
        postId: String?,
        isLike: String?
    ): Response<LikeDislikeResponse>? {
        return RetrofitClient().apiInterface.likeDislikeDetails(postId!!, isLike!!)
    }

    /** User Listing Api **/
    suspend fun userListingDetailsApi(
        pageNo: String?,
        perPage: String?,
        search: String
    ): Response<UserListingResponse>? {
        return RetrofitClient().apiInterface.userListingDetails(pageNo!!, perPage!!, search!!)
    }

    /**  Logout Api **/
    suspend fun logoutUser(body: LogoutRequest): Response<LogoutResponse>? {
        return RetrofitClient().apiInterface?.getLogout(body)
    }

    /** Share trip Api **/
    suspend fun shareTripDetailsApi(
        tripId: String?,
        userId: String?
    ): Response<ShareTripResponse>? {
        return RetrofitClient().apiInterface.shareTripDetails(tripId!!, userId!!)
    }

    /** Trip Diary Deleted Api **/
    suspend fun tripDiaryDeletedApi(
        diaryId: String?
    ): Response<TripDiaryDeleatedResponse>? {
        return RetrofitClient().apiInterface.tripDiaryDeleted(diaryId!!)
    }

    /** Location Api **/
    suspend fun locationEditApi(
        @Url url: String
    ): Response<PlacesModel>? {
        return RetrofitClient().apiInterface.placesAPiSerrvice(url)
    }

    /** LatLong Api **/
    suspend fun latLongApi(
        @Url url: String
    ): Response<PlacesLatLongModel>? {
        return RetrofitClient().apiInterface.placesLatlongSerrvice(url)
    }

    /** Trip Deleted Api **/
    suspend fun tripDeletedApi(
        tripId: String?,
        type: String?
    ): Response<TripDeleteResponse>? {
        return RetrofitClient().apiInterface.tripDeleted(tripId!!,type!!)
    }

    /** Post Deleted Api **/
    suspend fun postDeletedApi(
        postId: String?
    ): Response<PostDeleteResponse>? {
        return RetrofitClient().apiInterface.postDeleted(postId!!)
    }

}