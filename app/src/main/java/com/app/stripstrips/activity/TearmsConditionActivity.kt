package com.app.stripstrips.activity


import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.app.stripstrips.databinding.ActivityTearmsConditionBinding

class TearmsConditionActivity : AppCompatActivity() {
    lateinit var binding: ActivityTearmsConditionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTearmsConditionBinding.inflate(layoutInflater)
        onClick()
        setContentView(binding.root)
        binding.webView.webViewClient = WebViewClient()
        binding.webView.loadUrl("https://dharmani.com/StripTrip/termsAndConditions.php")
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.setSupportZoom(true)
    }

    private fun onClick() {
        binding.backIV.setOnClickListener {
            finish()
        }
    }
}