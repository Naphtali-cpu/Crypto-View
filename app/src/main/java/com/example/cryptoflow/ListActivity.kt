package com.example.cryptoflow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_list.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://api.coingecko.com/api/v3/"

class ListActivity : AppCompatActivity() {
    lateinit var myAdapter: CryptoListAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)


        recyclerviewlist.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(this)
        recyclerviewlist.layoutManager = linearLayoutManager
        getMyData()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.nav_home
        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_news -> {
                    startActivity(Intent(applicationContext, NewsActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_home -> return@OnNavigationItemSelectedListener true
                R.id.nav_settings -> {
                    startActivity(Intent(applicationContext, SettingsActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }

            }
            false
        })
    }

    private fun getMyData() {
        val okhttpHttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder().addInterceptor(
            okhttpHttpLoggingInterceptor
        )

        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient.build())
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getCrypto().enqueue(object :
            Callback<List<CryptoData>> {
            override fun onResponse(call: Call<List<CryptoData>>, response: Response<List<CryptoData>>) {
                hideProgressBar()
                if(response?.body() != null) {
                    myAdapter = CryptoListAdapter(baseContext, response.body()!!)
                    recyclerviewlist.adapter = myAdapter
                    myAdapter.notifyDataSetChanged()

                }
            }

            override fun onFailure(call: Call<List<CryptoData>>, t: Throwable) {
                hideProgressBar()
                Log.d("ListActivity", "onFailure:" + t.message)
            }
        })
    }
    private fun hideProgressBar() {
        progressBar.setVisibility(View.GONE)
    }
}