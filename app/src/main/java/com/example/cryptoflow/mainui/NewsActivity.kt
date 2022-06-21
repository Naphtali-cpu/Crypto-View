package com.example.cryptoflow.mainui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptoflow.R
import com.example.cryptoflow.adapters.NewsAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_news.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

const val BASE_URL2 = "https://api.polygon.io/v2/"


class NewsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        recyclerviewnews.layoutManager = LinearLayoutManager(this)
        fetchJson()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.nav_news
        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(applicationContext, ListActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_news -> return@OnNavigationItemSelectedListener true
                R.id.nav_settings -> {
                    startActivity(Intent(applicationContext, SettingsActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }

            }
            false
        })
    }

    private fun fetchJson() {
        println("Attempting to fetch JSON")

        val url = "https://api.polygon.io/v2/reference/news?apiKey=ilQSt9FKzshy2TT8ft25pwWqNhggcfmD"

        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object: okhttp3.Callback {
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                val body = response.body?.string()
                println(body)

                val gson = GsonBuilder().create()
                val homeFeed = gson.fromJson(body, HomeFeed::class.java)

                runOnUiThread {
                    recyclerviewnews.adapter = NewsAdapter(homeFeed)
                }

            }

            override fun onFailure(call: okhttp3.Call, e: IOException) {
                println("Failed to execute request")
            }

        })

    }



}

class HomeFeed(val results: List<Results>)

class Results(
    val amp_url: String,
    val article_url: String,
    val author: String,
    val description: String,
    val id: String,
    val image_url: String,
    val keywords: List<String>,
    val published_utc: String,
    val tickers: List<String>,
    val title: String

)