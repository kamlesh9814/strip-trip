package com.app.stripstrips.fragment

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import com.app.stripstrips.R
import com.app.stripstrips.base.MyApplication
import com.app.stripstrips.databinding.FragmentForgrtBinding
import com.app.stripstrips.model.forgrtPasswordApi.ForgetPasswordRequest
import com.app.stripstrips.network.Repository
import com.app.stripstrips.utils.ConstantsVar
import com.app.stripstrips.viewModel.ViewModel
import com.app.stripstrips.viewModel.ViewModelFactory

class ForgetFragment : Fragment(), View.OnClickListener {
    private val viewModel by viewModels<ViewModel>
    { ViewModelFactory(Application(), Repository()) }
    lateinit var binding: FragmentForgrtBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentForgrtBinding.inflate(layoutInflater, container, false)
        listeners()
        observer()
        return binding.root
    }


    private fun checkValidation(): Boolean {
        binding.apply {
            when {
                newPassword.text.toString().trim().isNullOrEmpty() -> {
                    Toast.makeText(requireContext(),
                        "please enter email",
                        Toast.LENGTH_SHORT).show()
                }
                !ConstantsVar.isEmailValid(newPassword.text.toString()) -> {
                    Toast.makeText(requireContext(), "please enter valid email", Toast.LENGTH_SHORT)
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
        binding.backImageView.setOnClickListener(this)
        binding.loginLayout.setOnClickListener(this)

    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {

            R.id.backImageView -> {
                requireActivity().supportFragmentManager.commit {
                    requireActivity().supportFragmentManager.popBackStack()
                }
            }
            R.id.loginLayout -> {
                if (checkValidation()) {
                    viewModel.getUserForgetPassword(ForgetPasswordRequest(binding.newPassword.text.toString()
                        .trim()))
                }
            }
        }
    }

    private fun observer() {
        viewModel.userForgetPassword.observe(requireActivity()) {
            when (it.status) {
                com.app.stripstrips.utils.Status.SUCCESS -> {
                    MyApplication.hideLoader()
                    requireActivity().supportFragmentManager.commit {
                        requireActivity().supportFragmentManager.popBackStack()
                    }
                        //Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    ConstantsVar.showFinishAlertDialog(requireActivity(),it.message.toString())
                }
                com.app.stripstrips.utils.Status.LOADING -> {
                    MyApplication.showLoader(requireContext())
                }
                com.app.stripstrips.utils.Status.ERROR -> {
                    MyApplication.hideLoader()
                    Log.e("exceptionError", it.message!!)
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}