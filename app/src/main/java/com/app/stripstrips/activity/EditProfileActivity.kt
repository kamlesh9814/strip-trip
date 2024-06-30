package com.app.stripstrips.activity

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.app.stripstrips.R
import com.app.stripstrips.base.MyApplication
import com.app.stripstrips.databinding.ActivityEditProfileBinding
import com.app.stripstrips.model.editprofileApi.EditProfileRequest
import com.app.stripstrips.network.Repository
import com.app.stripstrips.sharedPreferences.AppPrefs
import com.app.stripstrips.utils.Status
import com.app.stripstrips.viewModel.ViewModel
import com.app.stripstrips.viewModel.ViewModelFactory
import com.github.dhaval2404.imagepicker.ImagePicker
import okio.IOException

class   EditProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityEditProfileBinding
    private var userImage: Uri? = null
    var userId = ""

    private val viewModel by viewModels<ViewModel>
    { ViewModelFactory(Application(), Repository()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onClick()
        observer()
        setData()
        userId = AppPrefs(MyApplication.getContext()).getUserId("userID")!!
        binding.ivProfile.load(AppPrefs(this@EditProfileActivity).getString("userImage")) {
            error(R.drawable.human)
        }
    }

    private fun setData() {
        binding.nameET.setText(AppPrefs(this).getString("userName"))
        binding.emailET.setText(AppPrefs(this).getString("email"))
        binding.phoneET.setText(AppPrefs(this).getString("phoneNumber"))
        binding.ivProfile.load(Uri.parse(AppPrefs(this).getString("userImage")))
    }

    private fun checkValidation(): Boolean {
        binding.apply {
            when {
                (userImage.toString() == "") -> {
                    Toast.makeText(
                        this@EditProfileActivity,
                        "please upload profile image",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                nameET.text.toString().trim().isNullOrEmpty() -> {
                    Toast.makeText(
                        this@EditProfileActivity,
                        "please enter name",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                emailET.text.toString().trim().isNullOrEmpty() -> {
                    Toast.makeText(
                        this@EditProfileActivity,
                        "please enter email",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                phoneET.text.toString().trim().isNullOrEmpty() -> {
                    Toast.makeText(
                        this@EditProfileActivity,
                        "please enter phone number",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                phoneET.text.toString().trim().length < 10 || phoneET.text.toString()
                    .trim().length >= 14 -> {
                    Toast.makeText(
                        this@EditProfileActivity,
                        "Phone number must be at least 10 to 13 digits.",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                else -> {
                    return true
                }
            }
        }
        return false
    }

    private fun onClick() {
        binding.backImageView.setOnClickListener {
            onBackPressed()
        }
        binding.tvCamera.setOnClickListener {
            ImagePicker.with(this@EditProfileActivity)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start(111)
        }
        binding.saveLayout.setOnClickListener {
            if (checkValidation()) {
                viewModel.editProfile(
                    this@EditProfileActivity,
                    EditProfileRequest(
                        binding.nameET.text.toString().trim(),
                        binding.emailET.text.toString().trim(),
                        binding.phoneET.text.toString().trim(),
                        userImage
                    )
                )
            }
        }
    }

    private fun observer() {
        viewModel.editProfileData.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    MyApplication.hideLoader()

                    var data = it.data?.data
                    AppPrefs(MyApplication.getContext()).getUserId("userID")!!.isEmpty()
                    AppPrefs(this).setString("userName", data?.userName.toString())
                    AppPrefs(this).setString("email", data?.email.toString())
                    AppPrefs(this).setString("phoneNumber", data?.phoneNumber!!)
                    AppPrefs(this).setString("userImage", data.userImage.toString())
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    finish()

                }
                Status.LOADING -> {
                    MyApplication.showLoader(this)

                }
                Status.ERROR -> {
                    MyApplication.hideLoader()
                    Log.e("tag", it.message!!)
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 111 && resultCode == RESULT_OK && data != null) {
            try {
                userImage = data.data
                binding.ivProfile.load(userImage)
            } catch (e: IOException) {
                e.printStackTrace()
            }

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()

        } else {
            //Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }
}