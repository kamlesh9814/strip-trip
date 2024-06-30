package com.app.stripstrips.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.app.stripstrips.R
import com.app.stripstrips.sharedPreferences.AppPrefs
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging

@Suppress("DEPRECATION")
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    var TAG = "SplashActivity"
    var actionId = ""
    var message = ""
    var title = ""
    var from = ""
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setStatusBar(Color.WHITE)
        getDeviceToken()

        if (intent != null && intent.extras != null && !intent.extras!!.keySet().contains("profile")
        ) {

            if (intent.extras!!.keySet().contains("message")) {
                message = intent.extras!!.get("message").toString()
            }
            if (intent.extras!!.keySet().contains("actionId")) {
                actionId = intent.extras!!.get("actionId").toString()
            }
            if (intent.extras!!.keySet().contains("title")) {
                title = intent.extras!!.get("title").toString()
            }

            val intent = Intent(this@SplashActivity, TripsDetailsLocationActivity::class.java)
            intent.putExtra("Trip_Id", actionId)
            intent.putExtra("from", from)
            startActivity(intent)
            finish()

        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                sessionMaintain()
            }, 2500)
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setStatusBar(mColor: Int) {
        window.statusBarColor = mColor
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }

    private fun sessionMaintain() {
        if (AppPrefs(this@SplashActivity).getLoggedIn("loggedIn")!! == "yes") {
            startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
            finishAffinity()

        } else {
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finishAffinity()
        }
    }

    private fun getDeviceToken() {

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
            Log.d("Firebase", "token $token");
            Log.d(TAG, token)
        })
    }
}