package com.example.cryptoflow.mainui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoflow.R
import com.example.cryptoflow.adapters.CryptoListAdapter
import com.example.cryptoflow.adapters.PostAdapter
import com.example.cryptoflow.api.ApiInterface
import com.example.cryptoflow.data.CryptoData
import com.example.cryptoflow.data.Post
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_posts.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://api.coingecko.com/api/v3/"
class PostsActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    lateinit var myAdapter: CryptoListAdapter
    lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts)
        recyclerviewlist.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(this)
        recyclerviewlist.layoutManager = linearLayoutManager
        markets.setOnClickListener {
            getMyData()
        }
        getMyData()
        bottomBar()
        page2.setOnClickListener {
            loadPage2()
        }

    }

    private fun loadPage2() {
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

        val retrofitData = retrofitBuilder.getCrypto(100, 2).enqueue(object :
            Callback<List<CryptoData>> {
            override fun onResponse(
                call: Call<List<CryptoData>>,
                response: Response<List<CryptoData>>
            ) {
                hideProgressBar()
                myAdapter = CryptoListAdapter(baseContext, response.body()!!)
                recyclerviewlist.adapter = myAdapter
                myAdapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<List<CryptoData>>, t: Throwable) {
                hideProgressBar()
                Toast.makeText(applicationContext, "Check Your Internet Connection!", Toast.LENGTH_LONG).show()
                Log.d("ListActivity", "onFailure:" + t.message)
            }
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

        val retrofitData = retrofitBuilder.getCrypto(100, 1).enqueue(object :
            Callback<List<CryptoData>> {
            override fun onResponse(
                call: Call<List<CryptoData>>,
                response: Response<List<CryptoData>>
            ) {
                hideProgressBar()
                myAdapter = CryptoListAdapter(baseContext, response.body()!!)
                recyclerviewlist.adapter = myAdapter
                myAdapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<List<CryptoData>>, t: Throwable) {
                hideProgressBar()
                Toast.makeText(applicationContext, "Check Your Internet Connection!", Toast.LENGTH_LONG).show()
                Log.d("ListActivity", "onFailure:" + t.message)
            }
        })
    }

    private fun hideProgressBar() {
        cryptoLoader.visibility = View.GONE
    }

    private fun bottomBar() {

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.nav_network
        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_news -> {
                    startActivity(Intent(applicationContext, NewsActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_network -> return@OnNavigationItemSelectedListener true
                R.id.nav_home -> {
                    startActivity(Intent(applicationContext, ListActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(applicationContext, Profile::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }

            }
            false
        })
    }
}