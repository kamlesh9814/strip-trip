package com.app.stripstrips.fragment

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import com.app.stripstrips.R
import com.app.stripstrips.activity.TearmsConditionActivity
import com.app.stripstrips.activity.WebViewActivity
import com.app.stripstrips.base.MyApplication
import com.app.stripstrips.databinding.FragmentSignUpBinding
import com.app.stripstrips.model.signupApi.SignUpRequest
import com.app.stripstrips.network.Repository
import com.app.stripstrips.utils.ConstantsVar
import com.app.stripstrips.utils.ConstantsVar.showFinishAlertDialog
import com.app.stripstrips.utils.Status
import com.app.stripstrips.viewModel.ViewModel
import com.app.stripstrips.viewModel.ViewModelFactory

class SignUpFragment : Fragment(), View.OnClickListener {
    lateinit var binding: FragmentSignUpBinding
    var clicked = "0"

    private val viewModel by viewModels<ViewModel>
    { ViewModelFactory(Application(), Repository()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View{
        binding = FragmentSignUpBinding.inflate(layoutInflater, container, false)
        listeners()
        observer()
        return binding.root

    }

    private fun checkValidation(): Boolean {
        binding.apply {
            when {
                userName.text.toString().trim().isNullOrEmpty() -> {
                    Toast.makeText(requireContext(), "please enter name", Toast.LENGTH_SHORT).show()
                }
                email.text.toString().trim().isNullOrEmpty() -> {
                    Toast.makeText(requireContext(), "please enter email", Toast.LENGTH_SHORT)
                        .show()
                }
                !ConstantsVar.isEmailValid(email.text.toString()) -> {
                    Toast.makeText(requireContext(), "please enter valid email", Toast.LENGTH_SHORT)
                        .show()
                }
                phoneNumber.text.toString().trim().isNullOrEmpty() -> {
                    Toast.makeText(
                        requireContext(),
                        "please enter phone number",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                phoneNumber.text.toString().trim().length < 10 -> {
                    Toast.makeText(
                        requireContext(),
                        "Please Enter Valid Phone Number ",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

                Password.text.toString().trim().isNullOrEmpty() -> {
                    Toast.makeText(requireContext(), "please enter password", Toast.LENGTH_SHORT)
                        .show()
                }
                Password.text.toString().trim().length < 6 -> {
                    Toast.makeText(
                        requireContext(),
                        "Password must be at least 6 digits.",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                clicked != "1" -> {
                    Toast.makeText(
                        requireContext(),
                        "Please Accept Terms & Conditions",
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

    private fun listeners() {
        binding.SignUpLayout.setOnClickListener(this)
        binding.loginTV.setOnClickListener(this)
        binding.backImageView.setOnClickListener(this)
        binding.rememberMeImageView.setOnClickListener(this)
        binding.rememberMeTextView.setOnClickListener(this)

    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.backImageView -> {
                requireActivity().supportFragmentManager.commit {
                    requireActivity().supportFragmentManager.popBackStack()
                }
            }
            R.id.loginTV -> {
                requireActivity().supportFragmentManager.commit {
                    requireActivity().supportFragmentManager.popBackStack()
                }
            }
            R.id.SignUpLayout -> {
                if (checkValidation()) {
                    viewModel.getSignUp(
                        SignUpRequest(
                            binding.userName.text.toString().trim(),
                            binding.email.text.toString().trim(),
                            binding.phoneNumber.text.toString().trim(),
                            binding.Password.text.toString().trim()
                        )
                    )
                }
            }
            R.id.rememberMeImageView -> {
                clicked = if (clicked == "0") {
                    binding.rememberMeImageView.setImageDrawable(
                        (ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.check_circle
                        ))
                    )
                    "1"
                } else {
                    binding.rememberMeImageView.setImageDrawable(
                        (ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.remember_me_empty
                        ))
                    )
                    "0"
                }
            }
            R.id.rememberMeTextView -> {
                startActivity(Intent(requireActivity(), TearmsConditionActivity::class.java))
            }
        }
    }

    private fun observer() {
        viewModel.userSignUp.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    MyApplication.hideLoader()
                    showFinishAlertDialog(requireActivity(), it.message.toString())
                    requireActivity().supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace<LoginFragment>(R.id.frame_container)
                    }
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