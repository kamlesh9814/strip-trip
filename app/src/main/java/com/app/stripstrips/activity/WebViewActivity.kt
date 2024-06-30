package com.app.stripstrips.activity

import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.app.stripstrips.databinding.ActivityBebViewBinding

class WebViewActivity : AppCompatActivity() {
    lateinit var binding: ActivityBebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBebViewBinding.inflate(layoutInflater)
        onClick()
        setContentView(binding.root)

        binding.webView.webViewClient = WebViewClient()
        binding.webView.loadUrl("https://dharmani.com/StripTrip/privacyPolicy.php")
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.setSupportZoom(true)
    }

    private fun onClick() {
        binding.backIV.setOnClickListener {
            finish()
        }
    }

    override fun onBackPressed() {

        if (binding.webView.canGoBack())
            binding.webView.goBack()
        else
            super.onBackPressed()
    }
}