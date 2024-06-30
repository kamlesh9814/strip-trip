package com.app.stripstrips.fragment

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import com.app.stripstrips.R
import com.app.stripstrips.activity.HomeActivity
import com.app.stripstrips.base.MyApplication
import com.app.stripstrips.databinding.FragmentLoginBinding
import com.app.stripstrips.model.loginApi.LoginRequest
import com.app.stripstrips.network.Repository
import com.app.stripstrips.sharedPreferences.AppPrefs
import com.app.stripstrips.sharedPreferences.LoginPreferences
import com.app.stripstrips.utils.ConstantsVar
import com.app.stripstrips.utils.Status
import com.app.stripstrips.viewModel.ViewModel
import com.app.stripstrips.viewModel.ViewModelFactory
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging

class LoginFragment : Fragment(), View.OnClickListener {
    lateinit var binding: FragmentLoginBinding
    var mType = "0"
    var remember: String = "true"
    var TAG = "LoginFragment"
    var deviceToken = ""

    private val viewModel by viewModels<ViewModel>
    { ViewModelFactory(Application(), Repository()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getDeviceToken()

    }

    override fun onResume() {
        super.onResume()
        binding.emailET.text?.clear()
        binding.conformPassword.text?.clear()
        getData()

    }

    /*    private fun getDeviceToken() {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener {
                if (it.isSuccessful) {
                    deviceTokenUser = it.result!!
                    Log.e(ContentValues.TAG, "DeviceToken:$deviceTokenUser")
                } else {
                    return@OnCompleteListener
                }
            })
        }*/

    private fun getDeviceToken() {
        //  Log.d("Firebase:", "token "+ FirebaseInstanceId.getInstance().token)
        FirebaseInstallations.getInstance().id.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.e("Installations", "Installation ID: " + task.result)
            } else {
                Log.e("Installations", "Unable to get Installation ID")
            }
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            deviceToken = token
            // Log and toast
            Log.d(TAG, token)
            //Toast.makeText(requireContext(), token, Toast.LENGTH_SHORT).show()
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        getDeviceToken()
        listeners()
        if (ConstantsVar.isNetworkAvailable(requireContext())) {
            observer()
        } else {
            Toast.makeText(requireActivity(), "No Internet Connection", Toast.LENGTH_SHORT)
                .show()
        }
        return binding.root
    }

    private fun checkValidation(): Boolean {
        binding.apply {
            when {
                emailET.text.toString().trim().isNullOrEmpty() -> {
                    Toast.makeText(requireContext(), "please enter email", Toast.LENGTH_SHORT)
                        .show()
                }

                !ConstantsVar.isEmailValid(emailET.text.toString()) -> {
                    Toast.makeText(
                        requireContext(),
                        "please enter valid email",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

                conformPassword.text.toString().trim().isNullOrEmpty() -> {
                    Toast.makeText(
                        requireContext(),
                        "please enter password",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

                conformPassword.text.toString().trim().length < 6 -> {
                    Toast.makeText(
                        requireContext(),
                        "Password must be at least 6 digits.",
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

    private fun listeners() {
        binding.signupTextView.setOnClickListener(this)
        binding.loginLayout.setOnClickListener(this)
        binding.forgotPasswordTextView.setOnClickListener(this)
        binding.rememberMeImageView.setOnClickListener(this)
        binding.loginLayout.setOnClickListener(this)
        binding.emailET.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.signupTextView -> {
                requireActivity().supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    addToBackStack("login")
                    replace<SignUpFragment>(R.id.frame_container)
                }
            }
            R.id.forgotPasswordTextView -> {
                requireActivity().supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    addToBackStack("login")
                    replace<ForgetFragment>(R.id.frame_container)
                }
            }
            R.id.loginLayout -> {
                if (checkValidation()) {
                    viewModel.getLogin(
                        LoginRequest(
                            binding.emailET.text.toString().trim(),
                            binding.conformPassword.text.toString().trim(),
                            "2",
                            deviceToken
                        )
                    )
                }
            }
            R.id.rememberMeImageView -> {
                if (mType == "0") {
                    if (remember == "true") {
                        binding.rememberMeImageView.setImageDrawable(
                            ContextCompat.getDrawable(
                                requireActivity(),
                                R.drawable.check_circle
                            )
                        )
                        remember = "false"
                        LoginPreferences().writeString(
                            requireActivity(), ConstantsVar.REMEMBER_ME, "true"
                        )
                        LoginPreferences().writeString(
                            requireActivity(), ConstantsVar.EMAIL,
                            binding.emailET.text.toString()
                        )
                        LoginPreferences().writeString(
                            requireActivity(), ConstantsVar.PASSWORD,
                            binding.conformPassword.text.toString().trim()
                        )
                    } else {
                        binding.rememberMeImageView.setImageDrawable(
                            ContextCompat.getDrawable(
                                requireActivity(),
                                R.drawable.remember_me_empty
                            )
                        )
                        remember = "true"
                        LoginPreferences().writeString(
                            requireActivity(),
                            ConstantsVar.REMEMBER_ME,
                            "false"
                        )
                        LoginPreferences().writeString(
                            requireActivity(), ConstantsVar.EMAIL, ""
                        )
                        LoginPreferences().writeString(
                            requireActivity(), ConstantsVar.PASSWORD,
                            ""
                        )
                    }
                }
            }
        }
    }

    private fun getData() {

        if (LoginPreferences().readString(requireActivity(), ConstantsVar.REMEMBER_ME, null)
                .equals("true")
        ) {
            binding.emailET.setText(
                LoginPreferences().readString(
                    requireActivity(),
                    ConstantsVar.EMAIL,
                    null
                )
            )
            binding.conformPassword.setText(
                LoginPreferences().readString(
                    requireActivity(),
                    ConstantsVar.PASSWORD,
                    null
                )
            )
            LoginPreferences().writeString(requireActivity(), ConstantsVar.REMEMBER_ME, "true")
            binding.rememberMeImageView.setImageDrawable(
                ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.check_circle
                )
            )
        } else {
            binding.emailET.setText("")
            binding.conformPassword.setText("")
            LoginPreferences().writeString(requireActivity(), ConstantsVar.REMEMBER_ME, "false")
            binding.rememberMeImageView.setImageDrawable(
                ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.remember_me_empty
                )
            )
        }
    }

    private fun observer() {
        viewModel.userLogin.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    MyApplication.hideLoader()
                    Log.e(TAG, "observer: ${ it.data?.userData}")

                    AppPrefs(requireActivity()).setToken("token", it.data?.userData?.userToken.toString())
                    AppPrefs(requireActivity()).setLoggedIn("loggedIn", "yes")
                    AppPrefs(requireActivity()).setUserId("userID",it.data?.userData?.userId.toString())
                    AppPrefs(requireActivity()).setNameString("userName", it.data?.userData?.userName.toString())
                    AppPrefs(requireActivity()).setString("email",it.data?.userData?.email.toString())
                    AppPrefs(requireActivity()).setString("userImage", it.data?.userData?.userImage.toString())
                    it.data?.userData?.phoneNumber.toString()
                    AppPrefs(requireActivity()).setString("phoneNumber", it.data?.userData?.phoneNumber.toString())
                    startActivity(Intent(requireActivity(), HomeActivity::class.java))
                    ActivityCompat.finishAffinity(requireActivity())
                    Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()

                }
                Status.LOADING -> {
                    MyApplication.showLoader(requireActivity())

                }

                Status.ERROR -> {
                    MyApplication.hideLoader()
                    Log.e("exceptionError", it.message!!)
                    Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()

                }
            }
        }
    }
}