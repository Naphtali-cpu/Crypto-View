package com.example.cryptoflow.webview

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.webkit.*
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import com.example.cryptoflow.R
import com.example.cryptoflow.mainui.NewsActivity
import kotlinx.android.synthetic.main.activity_news_view.*


class NewsView : AppCompatActivity() {
//    private lateinit var webView: WebView

    var url = ""
//    private var webViewErrorLayout: RelativeLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_view)

//        webView = findViewById(R.id.webview)
//        webViewErrorLayout = findViewById(R.id.webErrorLayout)
        webview.webViewClient = WebViewClient()

        url = intent.getStringExtra("URL_LINK")!!
        Log.i("URLOG", url)
        webview.loadUrl(url)
        webview.settings.javaScriptEnabled = true

        webview.settings.setSupportZoom(true)

    }

    override fun onBackPressed() {
        val intent = Intent(this, NewsActivity::class.java)
        startActivity(intent)
    }
}