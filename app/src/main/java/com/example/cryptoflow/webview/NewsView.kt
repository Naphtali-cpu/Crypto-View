package com.example.cryptoflow.webview

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.webkit.*

import androidx.appcompat.app.AppCompatActivity
import com.example.cryptoflow.R


class NewsView : AppCompatActivity() {
    private lateinit var webView: WebView

    var url = ""
//    private var webViewErrorLayout: RelativeLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_view)

        webView = findViewById(R.id.webview)
//        webViewErrorLayout = findViewById(R.id.webErrorLayout)


        url = intent.getStringExtra("URL_LINK")!!
        webView.loadUrl(url)
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
            }

            override fun onReceivedError(
                view: WebView,
                request: WebResourceRequest,
                error: WebResourceError
            ) {
//                webView.setVisibility(View.GONE);
                webView.loadUrl("file:///android_asset/error.html")
                super.onReceivedError(view, request, error)
            }

            override fun onReceivedHttpError(
                view: WebView,
                request: WebResourceRequest,
                errorResponse: WebResourceResponse
            ) {
//                webView.loadUrl("file:///android_asset/error.html");
                super.onReceivedHttpError(view, request, errorResponse)
            }
        }
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
            }

            override fun onReceivedTitle(view: WebView, title: String) {
                super.onReceivedTitle(view, title)
            }

            override fun onReceivedIcon(view: WebView, icon: Bitmap) {
                super.onReceivedIcon(view, icon)
            }
        }
    }
}