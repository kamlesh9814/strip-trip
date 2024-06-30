package com.app.stripstrips.activity

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.app.stripstrips.base.MyApplication
import com.app.stripstrips.databinding.ActivityChangePasswordBinding
import com.app.stripstrips.model.changepasswordApi.ChangePasswordRequest
import com.app.stripstrips.network.Repository
import com.app.stripstrips.sharedPreferences.LoginPreferences
import com.app.stripstrips.utils.ConstantsVar
import com.app.stripstrips.utils.Status
import com.app.stripstrips.viewModel.ViewModel
import com.app.stripstrips.viewModel.ViewModelFactory


@Suppress("DEPRECATION")
class ChangePasswordActivity : AppCompatActivity() {
    lateinit var binding: ActivityChangePasswordBinding

    private val viewModel by viewModels<ViewModel>
    { ViewModelFactory(Application(), Repository()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observer()
        onClick()
    }

/*    private fun isValidate(): Boolean {
        var flag = true

        if (binding.oldPassword.text.toString().trim { it <= ' ' } == "") {
            Toast.makeText(
                this@ChangePasswordActivity,
                "please enter old password",
                Toast.LENGTH_SHORT
            ).show()
            flag = false

        } else if (binding.newPassword.text.toString().trim { it <= ' ' } == "") {
            Toast.makeText(
                this@ChangePasswordActivity,
                "please enter new password",
                Toast.LENGTH_SHORT
            ).show()
            flag = false

        } else if (binding.newPassword.text.toString().length < 6) {
            Toast.makeText(
                this@ChangePasswordActivity,
                "New password must be at least 6 digits.",
                Toast.LENGTH_SHORT
            ).show()

        } else if (binding.conformPassword.text.toString().trim { it <= ' ' } == "") {
            Toast.makeText(
                this@ChangePasswordActivity,
                "please enter confirm password",
                Toast.LENGTH_SHORT
            ).show()
            flag = false

        } else if (binding.conformPassword.text.toString()
                .trim { it <= ' ' } != binding.newPassword.text.toString().trim { it <= ' ' }
        ) {
            Toast.makeText(
                this@ChangePasswordActivity,
                "password does not match",
                Toast.LENGTH_SHORT
            ).show()
            flag = false

        }
        return flag
    }*/
    private fun isValidate(): Boolean {
        var flag = true
        if (binding.oldPassword.text.toString().trim { it <= ' ' } == "") {
            Toast.makeText(
                this@ChangePasswordActivity,
                "please enter old password",
                Toast.LENGTH_SHORT
            ).show()
            flag = false
        } else if (binding.newPassword.text.toString().trim { it <= ' ' } == "") {
            Toast.makeText(
                this@ChangePasswordActivity,
                "please enter new password",
                Toast.LENGTH_SHORT
            ).show()
            flag = false
        }
        else if (binding.newPassword.text.toString().trim().length<6) {
            Toast.makeText(
                this@ChangePasswordActivity,
                "New password must be at least 6 digits.",
                Toast.LENGTH_SHORT
            ).show()
            flag = false
        }
        else if (binding.conformPassword.text.toString().trim { it <= ' ' } == "") {
            Toast.makeText(
                this@ChangePasswordActivity,
                "please enter confirm password",
                Toast.LENGTH_SHORT
            ).show()
            flag = false
        } else if (binding.conformPassword.text.toString().trim { it <= ' ' } != binding.newPassword.text.toString().trim { it <= ' ' }
        ) {
            Toast.makeText(
                this@ChangePasswordActivity,
                "password does not match",
                Toast.LENGTH_SHORT
            ).show()
            flag = false
        }
        return flag
    }

    private fun onClick() {
        binding.backImageView.setOnClickListener {
            onBackPressed()
        }
        binding.saveLayout.setOnClickListener {
            if (isValidate()) {
                viewModel.getChangePassword(
                    ChangePasswordRequest(
                        binding.oldPassword.text.toString().trim(),
                        binding.newPassword.text.toString().trim(),
                    )
                )
            }
        }
    }

    private fun observer() {
        viewModel.userChangePassword.observe(this@ChangePasswordActivity) {
            when (it.status) {
                Status.SUCCESS -> {
                    MyApplication.hideLoader()
                    LoginPreferences().writeString(
                        this@ChangePasswordActivity, ConstantsVar.PASSWORD,
                        binding.conformPassword.text.toString().trim())
                    Toast.makeText(this@ChangePasswordActivity, it.message, Toast.LENGTH_SHORT).show()
                    finish()

                }
                Status.LOADING -> {
                    MyApplication.showLoader(this)

                }
                Status.ERROR -> {
                    MyApplication.hideLoader()
                    Log.e("exceptionError", it.message!!)
                    //   Log.e("Success", Gson().toJson(it))
                    Toast.makeText(this@ChangePasswordActivity, it.message, Toast.LENGTH_SHORT)
                        .show()

                }
            }
        }
    }
}