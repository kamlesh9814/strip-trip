package com.app.stripstrips.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.load
import com.app.stripstrips.R
import com.app.stripstrips.activity.ChangePasswordActivity
import com.app.stripstrips.activity.EditProfileActivity
import com.app.stripstrips.activity.TearmsConditionActivity
import com.app.stripstrips.activity.WebViewActivity
import com.app.stripstrips.databinding.FragmentProfileBinding
import com.app.stripstrips.sharedPreferences.AppPrefs

class ProfileFragment : Fragment(), View.OnClickListener {
    lateinit var binding: FragmentProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        listeners()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.tvFraoouLkosa.text = AppPrefs(requireActivity()).getString("userName")
        binding.tvfraundaFacebook.text = AppPrefs(requireActivity()).getString("email")
        binding.ivProfile.load(AppPrefs(requireActivity()).getString("userImage"))
        binding.ivProfile.load(AppPrefs(requireActivity()).getString("userImage")) {
            error(R.drawable.human)
        }
    }

    private fun listeners() {
        binding.ccl1.setOnClickListener(this)
        binding.IVEditProfile.setOnClickListener(this)
        binding.tvChangePayment.setOnClickListener(this)
        binding.ccl3.setOnClickListener(this)
        binding.ccl5.setOnClickListener(this)
        binding.ccl4.setOnClickListener(this)

    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.ccl1 -> {
                startActivity(Intent(requireContext(), ChangePasswordActivity::class.java))
            }
            R.id.IVEditProfile -> {
                startActivity(Intent(requireContext(), EditProfileActivity::class.java))
            }

            R.id.ccl3 -> {
                startActivity(Intent(requireContext(), WebViewActivity::class.java))

            }
            R.id.ccl4 -> {
                startActivity(Intent(requireContext(), TearmsConditionActivity::class.java))

            }

            R.id.ccl5 -> {
                val editProfileDialog = LogoutDialogueFragment()
                editProfileDialog.show(childFragmentManager, "null")
            }
        }
    }
}