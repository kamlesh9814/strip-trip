package com.app.stripstrips.activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import coil.load
import com.app.stripstrips.R
import com.app.stripstrips.databinding.ActivityHomeBinding
import com.app.stripstrips.fragment.*
import com.app.stripstrips.sharedPreferences.AppPrefs
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class HomeActivity : AppCompatActivity() {
    var TAG="HomeActivity"
    lateinit var binding:ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)


        colorChange()
        switchFragment(HomeFragment(), "", true, null)
        binding.ivHome.setColorFilter(ContextCompat.getColor(this@HomeActivity, R.color.lighe_green))
        setContentView(binding.root)

    }

    override fun onResume() {
        super.onResume()
        binding.ivProfile.load(AppPrefs(this@HomeActivity).getString("userImage")) {
            error(R.drawable.human)
        }
    }

    private fun colorChange() {

        binding.ivHome.setOnClickListener {
            switchFragment(HomeFragment(), "", true, null)
            binding.ivHome.setColorFilter(ContextCompat.getColor(this@HomeActivity, R.color.lighe_green))
            binding.ivLocation.setColorFilter(ContextCompat.getColor(this@HomeActivity, R.color.light_gray))
            binding.ivPlus.setColorFilter(ContextCompat.getColor(this@HomeActivity, R.color.light_gray))
            binding.ivDiary.setColorFilter(ContextCompat.getColor(this@HomeActivity, R.color.light_gray))
            var highlight = ContextCompat.getDrawable( this@HomeActivity,R.drawable.transparent_stork)
            binding.ivProfile.background = highlight
        }
        binding.ivLocation.setOnClickListener {
            switchFragment(TripsFragment(), "", true, null)
            binding.ivHome.setColorFilter(ContextCompat.getColor(this@HomeActivity, R.color.light_gray))
            binding.ivLocation.setColorFilter(ContextCompat.getColor(this@HomeActivity, R.color.lighe_green))
            binding.ivPlus.setColorFilter(ContextCompat.getColor(this@HomeActivity, R.color.light_gray))
            binding.ivDiary.setColorFilter(ContextCompat.getColor(this@HomeActivity, R.color.light_gray))
            var highlight = resources.getDrawable( R.drawable.transparent_stork)
            binding.ivProfile.background = highlight
        }
        binding.ivPlus.setOnClickListener {
            switchFragment(AddPostFragment(), "", true, null)
            binding.ivHome.setColorFilter(ContextCompat.getColor(this@HomeActivity, R.color.light_gray))
            binding.ivLocation.setColorFilter(ContextCompat.getColor(this@HomeActivity, R.color.light_gray))
            binding.ivPlus.setColorFilter(ContextCompat.getColor(this@HomeActivity, R.color.lighe_green))
            binding.ivDiary.setColorFilter(ContextCompat.getColor(this@HomeActivity, R.color.light_gray))
            var highlight = resources.getDrawable( R.drawable.transparent_stork)
            binding.ivProfile.background = highlight
        }
        binding.ivDiary.setOnClickListener {
            switchFragment(TripsDiaryFragment(), "", true, null)
            binding.ivHome.setColorFilter(ContextCompat.getColor(this@HomeActivity, R.color.light_gray))
            binding.ivLocation.setColorFilter(ContextCompat.getColor(this@HomeActivity, R.color.light_gray))
            binding.ivPlus.setColorFilter(ContextCompat.getColor(this@HomeActivity, R.color.light_gray))
            binding.ivDiary.setColorFilter(ContextCompat.getColor(this@HomeActivity, R.color.lighe_green))
            var highlight = resources.getDrawable( R.drawable.transparent_stork)
            binding.ivProfile.background = highlight
        }
        binding.ivProfile.setOnClickListener {
            switchFragment(ProfileFragment(), "", true, null)
            binding.ivHome.setColorFilter(ContextCompat.getColor(this@HomeActivity, R.color.light_gray))
            binding.ivLocation.setColorFilter(ContextCompat.getColor(this@HomeActivity, R.color.light_gray))
            binding.ivPlus.setColorFilter(ContextCompat.getColor(this@HomeActivity, R.color.light_gray))
            binding.ivDiary.setColorFilter(ContextCompat.getColor(this@HomeActivity, R.color.light_gray))
            var highlight = resources.getDrawable( R.drawable.profile_drawable)
            binding.ivProfile.background = highlight
        }
    }

    private fun switchFragment(fragment: Fragment?, Tag: String?, addToStack: Boolean, bundle: Bundle?) {
        val fragmentManager: FragmentManager = supportFragmentManager
        if (fragment != null) {
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.main_container, fragment, Tag)
            fragmentManager.popBackStack()
            if (bundle != null) fragment.arguments = bundle
            fragmentTransaction.commit()
            fragmentManager.executePendingTransactions()
        }
    }
}