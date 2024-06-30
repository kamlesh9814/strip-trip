package com.app.stripstrips.activity

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import com.app.stripstrips.R
import com.app.stripstrips.base.MyApplication
import com.app.stripstrips.databinding.ActivityAddTripsDiaryBinding
import com.app.stripstrips.model.addEditTripDiaryApi.AddEditTripsDiaryRequest
import com.app.stripstrips.network.Repository
import com.app.stripstrips.sharedPreferences.AppPrefs
import com.app.stripstrips.utils.Status
import com.app.stripstrips.viewModel.ViewModel
import com.app.stripstrips.viewModel.ViewModelFactory
import com.github.dhaval2404.imagepicker.ImagePicker
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.IOException
import java.io.ByteArrayOutputStream

@Suppress("DEPRECATION")
class AddTripsDiaryActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddTripsDiaryBinding
    private var REQUEST_CODE = 2004
    private var diaryId = ""
    private var diaryDescription = ""
    private var diaryName = ""
    private var diaryImage = ""
    private var isProfileSelected = false
    var bitmapProfile: Bitmap? = null
    private var mLastClickTime: Long = 0
    var mMultipartBodyOriginalBitmap: MultipartBody.Part? = null

    var TAG = "AddTripsDiaryActivity"
    private val viewModel by viewModels<ViewModel>
    { ViewModelFactory(Application(), Repository()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTripsDiaryBinding.inflate(layoutInflater)
        isProfileSelected = false
        observer()
        onClick()
        setContentView(binding.root)
        getIntentData()

    }

    private fun getIntentData() {
        if (intent != null && intent.hasExtra("tripName")) {
            diaryId = intent.getStringExtra("tripDiaryId").toString()
            diaryImage = intent.getStringExtra("tripImage").toString()
            diaryName = intent.getStringExtra("tripName").toString()
            diaryDescription = intent.getStringExtra("tripDescreption").toString()
            if (diaryId != "") {
                setWidgetData()

            }
            Log.e(TAG, "getIntentData: $diaryId")

        } else {
            binding.ivProfile.load(AppPrefs(this@AddTripsDiaryActivity).getString("")) {
                error(R.drawable.human)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun setWidgetData() {
        binding.tvAddTrips.text = "Edit Trip Diary"
        binding.tripNameET.setText(diaryName)
        binding.descriptionET.setText(diaryDescription)
        binding.loginTextView.text = "Update"

        val loader = ImageLoader(this)
        val req = ImageRequest.Builder(this)
            .data(diaryImage) // demo link
            .target { result ->
                val bitmap = (result as BitmapDrawable).bitmap

                if (bitmap != null) {
                    binding.ivProfile.setImageBitmap(bitmap)

                    bitmapProfile = bitmap.copy(Bitmap.Config.RGB_565, false)

                    isProfileSelected = true

                    Log.e(TAG, "setWidgetData: $bitmap")

                }
            }
            .build()
        val disposable = loader.enqueue(req)

    }

    private fun checkValidation(): Boolean {
        binding.apply {
            when {
                (!isProfileSelected) -> {
                    Toast.makeText(
                        this@AddTripsDiaryActivity,
                        "Please upload addTrip diary image",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                tripNameET.text.toString().trim().isEmpty() -> {
                    Toast.makeText(
                        this@AddTripsDiaryActivity,
                        "Please enter trip name",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                descriptionET.text.toString().trim().isEmpty() -> {
                    Toast.makeText(
                        this@AddTripsDiaryActivity,
                        "Please enter trip description",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    return true
                }
            }
        }
        return false
    }

    private fun onClick() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        this.binding.tvCamera.setOnClickListener {
            ImagePicker.with(this@AddTripsDiaryActivity)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start(REQUEST_CODE)
        }
        binding.SignUpLayout.setOnClickListener {
            if (checkValidation()) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return@setOnClickListener
                }
                mLastClickTime = SystemClock.elapsedRealtime()
                getMultiPart()
                MyApplication.showLoader(this@AddTripsDiaryActivity)
                viewModel.addEditTripApi(
                    this@AddTripsDiaryActivity,
                    AddEditTripsDiaryRequest(
                        diaryId,
                        binding.tripNameET.text.toString(),
                        binding.descriptionET.text.toString(), mMultipartBodyOriginalBitmap
                    )
                )
            }
        }
    }

    private fun observer() {
        viewModel.addEditTrip.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    MyApplication.hideLoader()
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    finish()

                }
                Status.LOADING -> {
                    MyApplication.showLoader(this@AddTripsDiaryActivity)

                }
                Status.ERROR -> {
                    MyApplication.hideLoader()
                    Log.e("tag", it.message!!)
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            try {
                val imageStream = contentResolver.openInputStream(data.data!!)
                val selectedImage = BitmapFactory.decodeStream(imageStream)
                bitmapProfile = selectedImage
                binding.ivProfile.setImageBitmap(bitmapProfile)
                isProfileSelected = true

            } catch (e: IOException) {
                e.printStackTrace()
            }

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()

        } else {
            // Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getMultiPart() {

        if (bitmapProfile != null) {
            val requestFileOrignalBitmap: RequestBody? =
                convertBitmapToByteArrayUncompressed(bitmapProfile!!)?.let {
                    RequestBody.create(
                        "multipart/form-data".toMediaTypeOrNull(),
                        it
                    )
                }
            mMultipartBodyOriginalBitmap = requestFileOrignalBitmap?.let {
                MultipartBody.Part.createFormData(
                    "tripImage",
                    getAlphaNumericString()!!.toString() + ".jpeg",
                    it
                )
            }
        }
    }

    private fun convertBitmapToByteArrayUncompressed(bitmap: Bitmap): ByteArray? {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 40, stream)
        return stream.toByteArray()
    }

    private fun getAlphaNumericString(): String {
        val n = 20
        val AlphaNumericString = ("ABCDEFGHIJKLMNOPQRSTUVWXYZ" /*+ "0123456789"*/
                + "abcdefghijklmnopqrstuvxyz")

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

    override fun onDestroy() {
        super.onDestroy()
        isProfileSelected = false

    }
}