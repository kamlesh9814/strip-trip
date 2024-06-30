package com.app.stripstrips.viewModel

import android.content.Context
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.stripstrips.activity.TripDiaryActivity
import com.app.stripstrips.activity.TripsDetailsLocationActivity
import com.app.stripstrips.activity.TripsDiaryDetailsLocationActivity
import com.app.stripstrips.activity.UserShareListingActivity
import com.app.stripstrips.model.LatlongApiModel.PlacesLatLongModel
import com.app.stripstrips.model.LatlongApiModel.PlacesModel
import com.app.stripstrips.utils.StatusMessageModel
import com.app.stripstrips.model.addEditTripDiaryApi.AddEditTripDiaryResponse
import com.app.stripstrips.model.addEditTripDiaryApi.AddEditTripsDiaryRequest
import com.app.stripstrips.model.addPostApi.AddPostRequest
import com.app.stripstrips.model.addPostApi.AddPostResponse
import com.app.stripstrips.model.changepasswordApi.ChangePasswordRequest
import com.app.stripstrips.model.changepasswordApi.ChangePasswordResponse
import com.app.stripstrips.model.editprofileApi.EditProfileRequest
import com.app.stripstrips.model.editprofileApi.EditProfileResponse
import com.app.stripstrips.model.forgrtPasswordApi.ForgetPasswordRequest
import com.app.stripstrips.model.forgrtPasswordApi.ForgetPasswordResponse
import com.app.stripstrips.model.homeApi.HomeRequest
import com.app.stripstrips.model.homeApi.HomeResponse
import com.app.stripstrips.model.likeDislikeApi.LikeDislikeRequest
import com.app.stripstrips.model.likeDislikeApi.LikeDislikeResponse
import com.app.stripstrips.model.loginApi.LoginRequest
import com.app.stripstrips.model.loginApi.LoginResponse
import com.app.stripstrips.model.logoutApi.LogoutRequest
import com.app.stripstrips.model.logoutApi.LogoutResponse
import com.app.stripstrips.model.postDeleteApi.PostDeleteRequest
import com.app.stripstrips.model.postDeleteApi.PostDeleteResponse
import com.app.stripstrips.model.postDetailsApi.PostDetailsRequest
import com.app.stripstrips.model.postDetailsApi.PostDetailsResponse
import com.app.stripstrips.model.shareTripApi.ShareTripRequest
import com.app.stripstrips.model.shareTripApi.ShareTripResponse
import com.app.stripstrips.model.signupApi.SignUpRequest
import com.app.stripstrips.model.signupApi.SignUpResponse
import com.app.stripstrips.model.tripAndSharedByOtherListingApi.TripSharedByOtherListingRequest
import com.app.stripstrips.model.tripAndSharedByOtherListingApi.TripSharedByOtherListingResponse
import com.app.stripstrips.model.tripDeleteApi.TripDeleteRequest
import com.app.stripstrips.model.tripDeleteApi.TripDeleteResponse
import com.app.stripstrips.model.tripDetails.TripDetailsRequest
import com.app.stripstrips.model.tripDetails.TripDetailsResponse
import com.app.stripstrips.model.tripDiaryDeleatedApi.TripDiaryDeleatedRequest
import com.app.stripstrips.model.tripDiaryDeleatedApi.TripDiaryDeleatedResponse
import com.app.stripstrips.model.tripsDiaryDetailsApi.TripsDiaryDetailsRequest
import com.app.stripstrips.model.tripsDiaryDetailsApi.TripsDiaryDetailsResponse
import com.app.stripstrips.model.tripsDiaryListingApi.TripsDiaryListingRequest
import com.app.stripstrips.model.tripsDiaryListingApi.TripsDiaryListingResponse
import com.app.stripstrips.model.userListingApi.UserListingRequest
import com.app.stripstrips.model.userListingApi.UserListingResponse
import com.app.stripstrips.network.Repository
import com.app.stripstrips.utils.ConstantsVar
import com.app.stripstrips.utils.Resource
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.UnknownHostException

class ViewModel(private val repository: Repository = Repository()) : ViewModel() {

    /** SignUp Api **/
    val userSignUp = MutableLiveData<Resource<SignUpResponse>>()
    fun getSignUp(body: SignUpRequest) {
        userSignUp.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.signUpUser(
                    body.userName,
                    body.email,
                    body.phoneNumber,
                    body.password
                )
                withContext(Dispatchers.Main) {
                    if (response?.code() == 200) {
                        if (response.body()?.status == 1) {
                            userSignUp.value = Resource.success(
                                response.body()!!,
                                response.body()?.message.toString()
                            )
                        } else {
                            userSignUp.value =
                                Resource.error(
                                    null,
                                    response.body()?.message.toString(),
                                    response.body()?.status
                                )
                        }
                    } else {
                        val gson = Gson()
                        val type = object : TypeToken<StatusMessageModel>() {}.type
                        val mModel: StatusMessageModel? =
                            gson.fromJson(response?.errorBody()!!.charStream(), type)
                        userSignUp.value = Resource.error(null, mModel?.message, mModel!!.status)
                    }
                }
            } catch (t: Throwable) {
                withContext(Dispatchers.Main) {
                    if (t !is UnknownHostException) {
                        userSignUp.value =
                            Resource.error(null, t.message.toString(), null)
                    } else {
                        userSignUp.value = Resource.error(null, null, null)
                    }
                }
            }
        }
    }

    /** Login api **/
    val userLogin = MutableLiveData<Resource<LoginResponse>>()
    fun getLogin(body: LoginRequest) {
        userLogin.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.loginInUser(
                    body.email,
                    body.password,
                    body.deviceType,
                    body.deviceToken
                )
                Log.e("URL-----", response?.raw()?.request.toString())
                withContext(Dispatchers.Main) {
                    if (response?.code() == 200) {
                        if (response.body()?.status == 1) {
                            userLogin.value = Resource.success(
                                response.body()!!,
                                response.body()?.message.toString()
                            )
                        } else {
                            userLogin.value =
                                Resource.error(
                                    null,
                                    response.body()?.message.toString(),
                                    response.body()?.status
                                )
                        }
                    } else {
                        val gson = Gson()
                        val type = object : TypeToken<StatusMessageModel>() {}.type
                        val mModel: StatusMessageModel? =
                            gson.fromJson(response?.errorBody()!!.charStream(), type)
                        userLogin.value = Resource.error(null, mModel?.message, mModel!!.status)

                    }
                }
            } catch (t: Throwable) {
                withContext(Dispatchers.Main) {
                    if (t !is UnknownHostException) {
                        userLogin.value =
                            Resource.error(null, t.message.toString(), null)
                    } else {
                        userLogin.value = Resource.error(null, null, null)
                    }
                }
            }
        }
    }

    /** Forget Password Api **/
    val userForgetPassword = MutableLiveData<Resource<ForgetPasswordResponse>>()
    fun getUserForgetPassword(body: ForgetPasswordRequest) {
        userForgetPassword.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.forgetUser(body.email)
                Log.e("URL-----", response?.raw()?.request.toString())
                withContext(Dispatchers.Main) {
                    if (response?.code() == 200) {
                        if (response.body()?.status == 1) {
                            userForgetPassword.value = Resource.success(
                                response.body()!!,
                                response.body()?.message.toString()
                            )
                        } else {
                            userForgetPassword.value =
                                Resource.error(
                                    null,
                                    response.body()?.message.toString(),
                                    response.body()?.status
                                )
                        }

                    } else {
                        val gson = Gson()
                        val type = object : TypeToken<StatusMessageModel>() {}.type
                        val mModel: StatusMessageModel? =
                            gson.fromJson(response?.errorBody()!!.charStream(), type )
                        userForgetPassword.value = Resource.error(
                            null,
                            mModel?.message,
                            mModel!!.status
                        )
                    }
                }
            } catch (t: Throwable) {
                withContext(Dispatchers.Main) {
                    if (t !is UnknownHostException) {
                        userForgetPassword.value =
                            Resource.error(null, t.message.toString(), null)
                    } else {
                        userForgetPassword.value = Resource.error(null, null, null)
                    }
                }
            }
        }
    }

    /**Change Password Api **/
    val userChangePassword = MutableLiveData<Resource<ChangePasswordResponse>>()
    fun getChangePassword(body: ChangePasswordRequest) {
        userChangePassword.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.changePasswordUser(body.password, body.newPassword)
                Log.e("URL-----", response?.raw()?.request.toString())
                withContext(Dispatchers.Main) {
                    if (response?.code() == 200) {
                        if (response.body()?.status == 1) {
                            userChangePassword.value = Resource.success(
                                response.body()!!,
                                response.body()?.message.toString()
                            )
                        } else {
                            userChangePassword.value =
                                Resource.error(
                                    null,
                                    response.body()?.message.toString(),
                                    response.body()?.status
                                )
                        }
                    } else {
                        val gson = Gson()
                        val type = object : TypeToken<StatusMessageModel>() {}.type
                        val mModel: StatusMessageModel? =
                            gson.fromJson(response?.errorBody()!!.charStream(), type)
                        userChangePassword.value = Resource.error(null, mModel?.message,mModel!!.status)

                    }
                }
            } catch (t: Throwable) {
                withContext(Dispatchers.Main) {
                    if (t !is UnknownHostException) {
                        userChangePassword.value =
                            Resource.error(null, t.message.toString(), null)
                    } else {
                        userChangePassword.value = Resource.error(null, null,  null)
                    }
                }
            }
        }
    }


    /** Edit Profile Data Api **/
    val editProfileData = MutableLiveData<Resource<EditProfileResponse>>()
    fun editProfile(context: Context, body: EditProfileRequest) {
        editProfileData.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.editProfileUser(body, context)
                Log.e("URL-----", response?.raw()?.request.toString())
                withContext(Dispatchers.Main) {
                    if (response?.code() == 200) {
                        if (response.body()?.status == 1) {
                            editProfileData.value = Resource.success(
                                response.body()!!,
                                response.body()?.message.toString()
                            )
                        } else {
                            editProfileData.value =
                                Resource.error(
                                    null,
                                    response.body()?.message.toString(),
                                    response.body()?.status
                                )
                        }
                    } else {
                        val gson = Gson()
                        val type = object : TypeToken<StatusMessageModel>() {}.type
                        val mModel: StatusMessageModel? =
                            gson.fromJson(response?.errorBody()!!.charStream(), type)
                        editProfileData.value = Resource.error(null, mModel?.message, mModel!!.status)

                    }
                }
            } catch (t: Throwable) {
                withContext(Dispatchers.Main) {
                    if (t !is UnknownHostException) {
                        editProfileData.value =
                            Resource.error(null, t.message.toString(),null)
                    } else {
                        editProfileData.value = Resource.error(null, null, null)
                    }
                }
            }
        }
    }

    /** Add Post Api **/
    val addPostData = MutableLiveData<Resource<AddPostResponse>>()
    fun addPostApi(context: Context, body: AddPostRequest) {
        addPostData.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.e(
                    "TAGV",
                    "onClick: ${body.videoHeight} ${body.videoWidth} ${body.thumbImage} ${body.postImage} ${body.latitude} ${body.longitude} "
                )
                val response = repository.addPostUser(context, body)
                Log.e("addEditPost", response.raw().request.toString())
                withContext(Dispatchers.Main) {
                    if (response.code() == 200) {
                        if (response.body()?.status == 1) {
                            addPostData.value = Resource.success(
                                response.body()!!,
                                response.body()?.message.toString()
                            )
                        } else {
                            addPostData.value = Resource.error(
                                null,
                                response.body()?.message.toString(),
                                response.body()?.status
                            )
                        }
                    } else {
                        val gson = Gson()
                        val type = object : TypeToken<StatusMessageModel>() {}.type
                        val mModel: StatusMessageModel? =
                            gson.fromJson(response.errorBody()!!.charStream(), type)
                        addPostData.value = Resource.error(null, mModel?.message, mModel!!.status)

                    }
                }
            } catch (t: Throwable) {
                withContext(Dispatchers.Main) {
                    if (t !is UnknownHostException) {
                        addPostData.value =
                            Resource.error(null, t.message.toString(), null)
                    } else {
                        addPostData.value = Resource.error(null, null, null)
                    }
                }
            }
        }
    }


    /** Update Post Api **/
    val updatePostData = MutableLiveData<Resource<AddPostResponse>>()
    fun updatePostApi(context: Context, body: AddPostRequest) {
        updatePostData.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.updatePostUser(context, body)
                Log.e("addEditPost", response.raw().request.toString())
                withContext(Dispatchers.Main) {
                    if (response.code() == 200) {
                        if (response.body()?.status == 1) {
                            updatePostData.value = Resource.success(
                                response.body()!!,
                                response.body()?.message.toString()
                            )
                        } else {
                            updatePostData.value = Resource.error(
                                null, response.body()?.message.toString(), response.body()?.status
                            )
                        }
                    } else {
                        val gson = Gson()
                        val type = object : TypeToken<StatusMessageModel>() {}.type
                        val mModel: StatusMessageModel? =
                            gson.fromJson(response.errorBody()!!.charStream(), type)
                        updatePostData.value = Resource.error(null, mModel?.message, mModel!!.status)

                    }
                }
            } catch (t: Throwable) {
                withContext(Dispatchers.Main) {
                    if (t !is UnknownHostException) {
                        updatePostData.value =
                            Resource.error(null, t.message.toString(),null)
                    } else {
                        updatePostData.value = Resource.error(null, null, null)
                    }
                }
            }
        }
    }



    /** Home Page Api **/
    val homePageData = MutableLiveData<Resource<HomeResponse>>()
    fun dataHomePage(body: HomeRequest, requireContext: Context) {
        homePageData.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.homePageAllParams(body.pageNo, body.perPage)
                Log.e("URL-----", response?.raw()?.request.toString())
                withContext(Dispatchers.Main) {
                    if (response?.code() == 200) {
                        if (response.body()?.status == 1) {
                            homePageData.value = Resource.success(
                                response.body()!!,
                                response.body()?.message.toString()
                            )
                        } else {
                            homePageData.value =
                                Resource.error(
                                    null,
                                    response.body()?.message.toString(),
                                    response.body()?.status
                                )
                        }
                    } else if (response?.code()==401){
                        ConstantsVar.ClearDataDialogAlart(requireContext)
                    }

                    else {
                        val gson = Gson()
                        val type = object : TypeToken<StatusMessageModel>() {}.type
                        val mModel: StatusMessageModel? =
                            gson.fromJson(response?.errorBody()!!.charStream(), type)
                        homePageData.value = Resource.error(null, mModel?.message, mModel!!.status)

                    }
                }
            } catch (t: Throwable) {
                withContext(Dispatchers.Main) {
                    if (t !is UnknownHostException) {
                        homePageData.value =
                            Resource.error(null, t.message.toString(),null)
                    } else {
                        homePageData.value = Resource.error(null, null, null)
                    }
                }
            }
        }
    }

    /** addEdit Trip Diary Api **/
    val addEditTrip = MutableLiveData<Resource<AddEditTripDiaryResponse>>()
    fun addEditTripApi(context: Context, body: AddEditTripsDiaryRequest) {
        addPostData.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.addEditTripDiaryDetails(context, body)
                Log.e("URL-----", response?.raw()?.request.toString())
                withContext(Dispatchers.Main) {
                    if (response?.code() == 200) {
                        if (response.body()?.status == 1) {
                            addEditTrip.value = Resource.success(
                                response.body()!!,
                                response.body()?.message.toString()
                            )
                        } else {
                            addEditTrip.value = Resource.error(
                                null, response.body()?.message.toString(), response.body()?.status
                            )
                        }
                    } else {

                        val gson = Gson()
                        val type = object : TypeToken<StatusMessageModel>() {}.type
                        val mModel: StatusMessageModel? =
                            gson.fromJson(response?.errorBody()!!.charStream(), type)
                        addEditTrip.value = Resource.error(null, mModel?.message, mModel!!.status)
                    }
                }
            } catch (t: Throwable) {
                withContext(Dispatchers.Main) {
                    if (t !is UnknownHostException) {
                        addEditTrip.value =
                            Resource.error(null, t.message.toString(),null)
                    } else {
                        addEditTrip.value = Resource.error(null, null, null)
                    }
                }
            }
        }
    }

    /** trip Details Api **/
    val tripDetailsData = MutableLiveData<Resource<TripDetailsResponse>>()
    fun dataTripDetails(
        body: TripDetailsRequest,
        tripsDetailsLocationActivity: TripsDetailsLocationActivity
    ) {
        tripDetailsData.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.tripDetailsApi(body.tripId, body.pageNo, body.perPage)
                Log.e("URL-----", response?.raw()?.request.toString())
                withContext(Dispatchers.Main) {
                    if (response?.code() == 200) {
                        if (response.body()?.status == 1) {
                            tripDetailsData.value = Resource.success(
                                response.body()!!,
                                response.body()?.message.toString()
                            )
                        } else {
                            tripDetailsData.value =
                                Resource.error(
                                    null,
                                    response.body()?.message.toString(),
                                    response.body()?.status
                                )
                        }
                    }  else if (response?.code()==401) {
                        ConstantsVar.ClearDataDialogAlart(tripsDetailsLocationActivity)
                    }
                    else {
                        val gson = Gson()
                        val type = object : TypeToken<StatusMessageModel>() {}.type
                        val mModel: StatusMessageModel? =
                            gson.fromJson(response?.errorBody()!!.charStream(), type)
                        tripDetailsData.value = Resource.error(null, mModel?.message, mModel!!.status)

                    }
                }
            } catch (t: Throwable) {
                withContext(Dispatchers.Main) {
                    if (t !is UnknownHostException) {
                        tripDetailsData.value =
                            Resource.error(null, t.message.toString(), null)
                    } else {
                        tripDetailsData.value = Resource.error(null, null, null)
                    }
                }
            }
        }
    }

    /** trip Diary Listing Api **/
    val tripDiaryListingApi = MutableLiveData<Resource<TripsDiaryListingResponse>>()
    fun tripDiaryListingApiData(body: TripsDiaryListingRequest, requireActivity: FragmentActivity) {
        tripDiaryListingApi.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.tripDiaryListingApi(body.pageNo, body.perPage)
                Log.e("URL-----", response?.raw()?.request.toString())
                withContext(Dispatchers.Main) {
                    if (response?.code() == 200) {
                        if (response.body()?.status == 1) {
                            tripDiaryListingApi.value = Resource.success(
                                response.body()!!,
                                response.body()?.message.toString()
                            )
                        } else {
                            tripDiaryListingApi.value =
                                Resource.error(
                                    null,
                                    response.body()?.message.toString(),
                                    response.body()?.status
                                )
                        }
                    } else if (response?.code()==401){
                        ConstantsVar.ClearDataDialogAlart(requireActivity)
                    }
                    else {
                        val gson = Gson()
                        val type = object : TypeToken<StatusMessageModel>() {}.type
                        val mModel: StatusMessageModel? =
                            gson.fromJson(response?.errorBody()!!.charStream(), type)
                        tripDiaryListingApi.value = Resource.error(
                            null,
                            mModel?.message,
                            mModel!!.status
                        )
                    }
                }
            } catch (t: Throwable) {
                withContext(Dispatchers.Main) {
                    if (t !is UnknownHostException) {
                        tripDiaryListingApi.value =
                            Resource.error(null, t.message.toString(), null)
                    } else {
                        tripDiaryListingApi.value = Resource.error(null, null, null)
                    }
                }
            }
        }
    }

    /** trip Diary Details Api  **/
    val tripDiaryDetailsApi = MutableLiveData<Resource<TripsDiaryDetailsResponse>>()
    fun tripDiaryDetailsApiData(
        body: TripsDiaryDetailsRequest,
        tripsDiaryDetailsLocationActivity: TripsDiaryDetailsLocationActivity
    ) {
        tripDiaryDetailsApi.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    repository.tripDiaryDetailsApi(body.diaryId, body.pageNo, body.perPage)
                Log.e("URL-----", response?.raw()?.request.toString())
                withContext(Dispatchers.Main) {
                    if (response?.code() == 200) {
                        if (response.body()?.status == 1) {
                            tripDiaryDetailsApi.value = Resource.success(
                                response.body()!!,
                                response.body()?.message.toString()
                            )
                        } else {
                            tripDiaryDetailsApi.value =
                                Resource.error(
                                    null,
                                    response.body()?.message.toString(),
                                    response.body()?.status
                                )
                        }
                    } else if (response?.code()==401){
                        ConstantsVar.ClearDataDialogAlart(tripsDiaryDetailsLocationActivity)
                    }
                    else {
                        val gson = Gson()
                        val type = object : TypeToken<StatusMessageModel>() {}.type
                        val mModel: StatusMessageModel? =
                            gson.fromJson(response?.errorBody()!!.charStream(), type)
                        tripDiaryDetailsApi.value = Resource.error(
                            null,
                            mModel?.message,
                            mModel!!.status
                        )
                    }
                }
            } catch (t: Throwable) {
                withContext(Dispatchers.Main) {
                    if (t !is UnknownHostException) {
                        tripDiaryDetailsApi.value =
                            Resource.error(null, t.message.toString(), null)
                    } else {
                        tripDiaryDetailsApi.value = Resource.error(null, null, null)
                    }
                }
            }
        }
    }

    /** Trips Shared By Other Api **/
    val tripsSharedByOtherApi = MutableLiveData<Resource<TripSharedByOtherListingResponse>>()
    fun tripsSharedByOtherApiData(
        body: TripSharedByOtherListingRequest

    ) {
        tripsSharedByOtherApi.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    repository.sharedByOtherTripListApi(
                        body.pageNo,
                        body.perPage,
                        body.type,
                        body.search
                    )
                Log.e("URL-----", response?.raw()?.request.toString())
                withContext(Dispatchers.Main) {
                    if (response?.code() == 200) {
                        if (response.body()?.status == 1) {
                            tripsSharedByOtherApi.value = Resource.success(
                                response.body()!!,
                                response.body()?.message.toString()
                            )
                        } else {
                            tripsSharedByOtherApi.value =
                                Resource.error(
                                    null,
                                    response.body()?.message.toString(),
                                    response.body()?.status
                                )
                        }
                    }/* else if (response?.code()==401){
                        ConstantsVar.ClearDataDialogAlart(requireActivity)
                    }*/
                    else {
                        val gson = Gson()
                        val type = object : TypeToken<StatusMessageModel>() {}.type
                        val mModel: StatusMessageModel? =
                            gson.fromJson(response?.errorBody()!!.charStream(), type)
                        tripsSharedByOtherApi.value = Resource.error(
                            null,
                            mModel?.message,
                            mModel!!.status
                        )
                    }
                }
            } catch (t: Throwable) {
                withContext(Dispatchers.Main) {
                    if (t !is UnknownHostException) {
                        tripsSharedByOtherApi.value =
                            Resource.error(null, t.message.toString(), null)
                    } else {
                        tripsSharedByOtherApi.value = Resource.error(null, null, null)
                    }
                }
            }
        }
    }

    /** Like Dislike Api **/
    val likeDislikeApi = MutableLiveData<Resource<LikeDislikeResponse>>()
    fun likeDislikeDetailsApiData(body: LikeDislikeRequest) {
        likeDislikeApi.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.likeDislikeDetailsApi(body.postId, body.isLike)
                Log.e("URL-----", response?.raw()?.request.toString())
                withContext(Dispatchers.Main) {
                    if (response?.code() == 200) {
                        if (response.body()?.status == 1) {
                            likeDislikeApi.value = Resource.success(
                                response.body()!!,
                                response.body()?.message.toString()
                            )
                        } else if (response.body()?.status == 2)//trip creation
                        {
                            likeDislikeApi.value = Resource.success(
                                response.body()!!,
                                response.body()?.message.toString()
                            )
                        } else {
                            likeDislikeApi.value =
                                Resource.error(
                                    null,
                                    response.body()?.message.toString(),
                                    response.body()?.status
                                )
                        }
                    } else {
                        val gson = Gson()
                        val type = object : TypeToken<StatusMessageModel>() {}.type
                        val mModel: StatusMessageModel? =
                            gson.fromJson(response?.errorBody()!!.charStream(), type)
                        likeDislikeApi.value = Resource.error(null, mModel?.message, mModel!!.status)
                    }
                }
            } catch (t: Throwable) {
                withContext(Dispatchers.Main) {
                    if (t !is UnknownHostException) {
                        likeDislikeApi.value =
                            Resource.error(null, t.message.toString(),null)
                    } else {
                        likeDislikeApi.value = Resource.error(null, null, null)
                    }
                }
            }
        }
    }

    /** User Listing Api **/
    val userListingApi = MutableLiveData<Resource<UserListingResponse>>()
    fun userListingDetailsApiData(body: UserListingRequest) {
        userListingApi.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    repository.userListingDetailsApi(body.pageNo!!, body.perPage!!, body.search!!)
                Log.e("URL-----", response?.raw()?.request.toString())
                withContext(Dispatchers.Main) {
                    if (response?.code() == 200) {
                        if (response.body()?.status == 1) {
                            userListingApi.value = Resource.success(
                                response.body()!!,
                                response.body()?.message.toString()
                            )
                        } else {
                            userListingApi.value =
                                Resource.error(
                                    null,
                                    response.body()?.message.toString(),
                                    response.body()?.status
                                )
                        }
                    } else {
                        val gson = Gson()
                        val type = object : TypeToken<StatusMessageModel>() {}.type
                        val mModel: StatusMessageModel? =
                            gson.fromJson(response?.errorBody()!!.charStream(), type)
                        userListingApi.value = Resource.error(null, mModel?.message, mModel!!.status)
                    }
                }
            } catch (t: Throwable) {
                withContext(Dispatchers.Main) {
                    if (t !is UnknownHostException) {
                        userListingApi.value =
                            Resource.error(null, t.message.toString(), null)
                    } else {
                        userListingApi.value = Resource.error(null, null,null)
                    }
                }
            }
        }
    }

    /** Logout Api **/
    val getUserLogout = MutableLiveData<Resource<LogoutResponse>>()
    fun logoutData(body: LogoutRequest) {
        getUserLogout.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.logoutUser(body)
                Log.e("URL-----", response?.raw()?.request.toString())
                withContext(Dispatchers.Main) {
                    if (response?.code() == 200) {
                        if (response.body()?.status == 1) {
                            getUserLogout.value = Resource.success(
                                response.body()!!,
                                response.body()?.message.toString()
                            )
                        } else {
                            getUserLogout.value =
                                Resource.error(
                                    null,
                                    response.body()?.message.toString(),
                                    response.body()?.status
                                )
                        }
                    } else {
                        val gson = Gson()
                        val type = object : TypeToken<StatusMessageModel>() {}.type
                        val mModel: StatusMessageModel? =
                            gson.fromJson(response?.errorBody()!!.charStream(), type)
                        getUserLogout.value = Resource.error(null, mModel?.message, mModel!!.status)
                    }
                }

            } catch (t: Throwable) {
                withContext(Dispatchers.Main) {
                    if (t !is UnknownHostException) {
                        getUserLogout.value =
                            Resource.error(null, t.message.toString(), null)
                    } else {
                        getUserLogout.value = Resource.error(null, null, null)
                    }
                }
            }
        }
    }

    /** User Share Listing Api **/
    val userShareTripApi = MutableLiveData<Resource<ShareTripResponse>>()
    fun userShareTripApiData(
        body: ShareTripRequest,
        userShareListingActivity: UserShareListingActivity
    ) {
        userShareTripApi.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.shareTripDetailsApi(body.tripId!!, body.userId!!)
                Log.e("URL-----", response?.raw()?.request.toString())
                withContext(Dispatchers.Main) {
                    if (response?.code() == 200) {
                        if (response.body()?.status == 1) {
                            userShareTripApi.value = Resource.success(
                                response.body()!!,
                                response.body()?.message.toString()
                            )
                        } else {
                            userShareTripApi.value =
                                Resource.error(
                                    null,
                                    response.body()?.message.toString(),
                                    response.body()?.status
                                )
                        }
                    }  else if (response?.code()==401){
                        ConstantsVar.ClearDataDialogAlart(userShareListingActivity)
                    }

                    else {
                        val gson = Gson()
                        val type = object : TypeToken<StatusMessageModel>() {}.type
                        val mModel: StatusMessageModel? =
                            gson.fromJson(response?.errorBody()!!.charStream(), type)
                        userShareTripApi.value = Resource.error(
                            null,
                            mModel?.message,
                            mModel!!.status
                        )
                    }
                }
            } catch (t: Throwable) {
                withContext(Dispatchers.Main) {
                    if (t !is UnknownHostException) {
                        userShareTripApi.value =
                            Resource.error(null, t.message.toString(), null)
                    } else {
                        userShareTripApi.value = Resource.error(null, null, null)
                    }
                }
            }
        }
    }

    /** User Share DeleteListing Api **/
    val tripDiaryDeletedApi = MutableLiveData<Resource<TripDiaryDeleatedResponse>>()
    fun userTripDiaryDeletedData(body: TripDiaryDeleatedRequest) {
        tripDiaryDeletedApi.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.tripDiaryDeletedApi(body.diaryId)
                Log.e("URL-----", response?.raw()?.request.toString())
                withContext(Dispatchers.Main) {
                    if (response?.code() == 200) {
                        if (response.body()?.status == 1) {
                            tripDiaryDeletedApi.value = Resource.success(
                                response.body()!!,
                                response.body()?.message.toString()
                            )
                        } else {
                            tripDiaryDeletedApi.value =
                                Resource.error(
                                    null,
                                    response.body()?.message.toString(),
                                    response.body()?.status
                                )
                        }
                    }

                    else {
                        val gson = Gson()
                        val type = object : TypeToken<StatusMessageModel>() {}.type
                        val mModel: StatusMessageModel? =
                            gson.fromJson(response?.errorBody()!!.charStream(), type)
                        tripDiaryDeletedApi.value = Resource.error(
                            null,
                            mModel?.message,
                            mModel!!.status
                        )
                    }
                }
            } catch (t: Throwable) {
                withContext(Dispatchers.Main) {
                    if (t !is UnknownHostException) {
                        tripDiaryDeletedApi.value =
                            Resource.error(null, t.message.toString(), null)
                    } else {
                        tripDiaryDeletedApi.value = Resource.error(null, null, null)
                    }
                }
            }
        }
    }

    /** Location Edit Api **/
    val locationEditApi = MutableLiveData<Resource<PlacesModel>>()
    fun locationEditApiData(body: String) {
        locationEditApi.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.locationEditApi(body)
                Log.e("URL-----", response?.raw()?.request.toString())
                withContext(Dispatchers.Main) {
                    if (response?.code() == 200) {
                        locationEditApi.value = Resource.success(
                            response.body()!!,
                            response.body()?.predictions.toString()
                        )

                    } else {
                        val gson = Gson()
                        val type = object : TypeToken<StatusMessageModel>() {}.type
                        val mModel: StatusMessageModel? =
                            gson.fromJson(response?.errorBody()!!.charStream(), type)
                        locationEditApi.value = Resource.error(null, mModel?.message, mModel!!.status)
                    }
                }
            } catch (t: Throwable) {
                withContext(Dispatchers.Main) {
                    if (t !is UnknownHostException) {
                        locationEditApi.value =
                            Resource.error(null, t.message.toString(), null)
                    } else {
                        locationEditApi.value = Resource.error(null, null, null)
                    }
                }
            }
        }
    }

    /** LatLong  Api **/
    val LatLongApi = MutableLiveData<Resource<PlacesLatLongModel>>()
    fun latLongEditApiData(body: String) {
        LatLongApi.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.latLongApi(body)
                Log.e("URL-----", response?.raw()?.request.toString())
                withContext(Dispatchers.Main) {
                    if (response?.code() == 200) {
                        LatLongApi.value = Resource.success(
                            response.body()!!,
                            response.body()?.result.toString()
                        )

                    } else {
                        val gson = Gson()
                        val type = object : TypeToken<StatusMessageModel>() {}.type
                        val mModel: StatusMessageModel? =
                            gson.fromJson(response?.errorBody()!!.charStream(), type)
                        LatLongApi.value = Resource.error(null, mModel?.message, mModel!!.status)
                    }
                }
            } catch (t: Throwable) {
                withContext(Dispatchers.Main) {
                    if (t !is UnknownHostException) {
                        LatLongApi.value =
                            Resource.error(null, t.message.toString(), null)
                    } else {
                        LatLongApi.value = Resource.error(null, null, null)
                    }
                }
            }
        }
    }

    /** User Share DeleteListing Api **/
    val tripDeletedApi = MutableLiveData<Resource<TripDeleteResponse>>()
    fun userTripDeletedData(body: TripDeleteRequest) {
        tripDeletedApi.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.tripDeletedApi(body.tripId,body.type)
                Log.e("URL-----", response?.raw()?.request.toString())
                withContext(Dispatchers.Main) {
                    if (response?.code() == 200) {
                        if (response.body()?.status == 1) {
                            tripDeletedApi.value = Resource.success(
                                response.body()!!,
                                response.body()?.message.toString()
                            )
                        } else {
                            tripDeletedApi.value =
                                Resource.error(
                                    null,
                                    response.body()?.message.toString(),
                                    response.body()?.status
                                )
                        }
                    } else {
                        val gson = Gson()
                        val type = object : TypeToken<StatusMessageModel>() {}.type
                        val mModel: StatusMessageModel? =
                            gson.fromJson(response?.errorBody()!!.charStream(), type)
                        tripDeletedApi.value = Resource.error(null, mModel?.message, mModel!!.status)
                    }
                }
            } catch (t: Throwable) {
                withContext(Dispatchers.Main) {
                    if (t !is UnknownHostException) {
                        tripDeletedApi.value =
                            Resource.error(null, t.message.toString(),null)
                    } else {
                        tripDeletedApi.value = Resource.error(null, null, null)
                    }
                }
            }
        }
    }
    /** Post Details Api **/
    val postDetailApi = MutableLiveData<Resource<PostDetailsResponse>>()
    fun userPostDetailsData(body: PostDetailsRequest, tripDiaryActivity: TripDiaryActivity) {
        postDetailApi.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.postDetailsApi(body.postId)
                Log.e("URL-----", response?.raw()?.request.toString())
                withContext(Dispatchers.Main) {
                    if (response?.code() == 200) {
                        if (response.body()?.status == 1) {
                            postDetailApi.value = Resource.success(
                                response.body()!!,
                                response.body()?.message.toString()
                            )
                        } else {
                            postDetailApi.value =
                                Resource.error(
                                    null,
                                    response.body()?.message.toString(),
                                    response.body()?.status
                                )
                        }
                    }  else if (response?.code()==401){
                        ConstantsVar.ClearDataDialogAlart(tripDiaryActivity)
                    }
                    else {
                        val gson = Gson()
                        val type = object : TypeToken<StatusMessageModel>() {}.type
                        val mModel: StatusMessageModel? =
                            gson.fromJson(response?.errorBody()!!.charStream(), type)
                        postDetailApi.value = Resource.error(null, mModel?.message, mModel!!.status)
                    }
                }
            } catch (t: Throwable) {
                withContext(Dispatchers.Main) {
                    if (t !is UnknownHostException) {
                        postDetailApi.value =
                            Resource.error(null, t.message.toString(), null)
                    } else {
                        postDetailApi.value = Resource.error(null, null, null)
                    }
                }
            }
        }
    }

    /** Post Details Api **/
    val postDeleteApi = MutableLiveData<Resource<PostDeleteResponse>>()
    fun userPostDeleteData(body: PostDeleteRequest) {
        postDeleteApi.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.postDeletedApi(body.postId)
                Log.e("URL-----", response?.raw()?.request.toString())
                withContext(Dispatchers.Main) {
                    if (response?.code() == 200) {
                        if (response.body()?.status == 1) {
                            postDeleteApi.value = Resource.success(
                                response.body()!!,
                                response.body()?.message.toString()
                            )
                        } else {
                            postDeleteApi.value =
                                Resource.error(
                                    null,
                                    response.body()?.message.toString(),
                                    response.body()?.status
                                )
                        }
                    } else {
                        val gson = Gson()
                        val type = object : TypeToken<StatusMessageModel>() {}.type
                        val mModel: StatusMessageModel? =
                            gson.fromJson(response?.errorBody()!!.charStream(), type)
                        postDeleteApi.value = Resource.error(null, mModel?.message, mModel!!.status)
                    }
                }
            } catch (t: Throwable) {
                withContext(Dispatchers.Main) {
                    if (t !is UnknownHostException) {
                        postDeleteApi.value =
                            Resource.error(null, t.message.toString(), null)
                    } else {
                        postDeleteApi.value = Resource.error(null, null, null)
                    }
                }
            }
        }
    }
}