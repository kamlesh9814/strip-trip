package com.app.stripstrips.fragment

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.app.stripstrips.R
import com.app.stripstrips.activity.MainActivity
import com.app.stripstrips.base.MyApplication
import com.app.stripstrips.databinding.FragmentLogoutDialogueBinding
import com.app.stripstrips.model.forgrtPasswordApi.ForgetPasswordRequest
import com.app.stripstrips.model.logoutApi.LogoutRequest
import com.app.stripstrips.network.Repository
import com.app.stripstrips.sharedPreferences.AppPrefs
import com.app.stripstrips.utils.ConstantsVar
import com.app.stripstrips.viewModel.ViewModel
import com.app.stripstrips.viewModel.ViewModelFactory

class LogoutDialogueFragment : DialogFragment() {
    lateinit var binding: FragmentLogoutDialogueBinding

    private val viewModel by viewModels<ViewModel>
    { ViewModelFactory(Application(), Repository()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observer()
        setStyle(STYLE_NO_TITLE, R.style.DialogTheme_transparent)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentLogoutDialogueBinding.inflate(layoutInflater, container, false)
        onClick()
        return binding.root
    }

    private fun onClick() {
        binding.tvNotNow.setOnClickListener {
            dismiss()
        }

        binding.loginLayout.setOnClickListener {
            viewModel.logoutData(LogoutRequest("1"))
            //dismiss()
            /*    AppPrefs(requireActivity()).clearData()
                requireActivity().startActivity(Intent(requireActivity(), MainActivity::class.java))
                requireActivity().finishAffinity()*/
        }
    }

    private fun observer() {
        viewModel.getUserLogout.observe(requireActivity()) {
            when (it.status) {
                com.app.stripstrips.utils.Status.SUCCESS -> {
                    MyApplication.hideLoader()
                    AppPrefs(requireActivity()).setLoggedIn("loggedIn", "no")
                    Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
                    startActivity(Intent(requireActivity(), MainActivity::class.java))
                    requireActivity().finishAffinity()
                }
                com.app.stripstrips.utils.Status.LOADING -> {
                    MyApplication.showLoader(requireActivity())

                }
                com.app.stripstrips.utils.Status.ERROR -> {
                    MyApplication.hideLoader()

                    Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}