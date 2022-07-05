package com.example.cryptoflow.mainui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoflow.R
import com.example.cryptoflow.adapters.CryptoListAdapter
import com.example.cryptoflow.adapters.NewsAdapter
import com.example.cryptoflow.api.ApiInterface
import com.example.cryptoflow.data.CryptoData
import com.example.cryptoflow.sessions.LoginPref
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.android.synthetic.main.activity_news.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

const val BASE_URL = "https://api.coingecko.com/api/v3/"

class ListActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    lateinit var session: LoginPref
    lateinit var myAdapter: CryptoListAdapter
    lateinit var linearLayoutManager: LinearLayoutManager

    var page = 1
    var isLoading = false
    var limit = 10
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        recyclerviewlist.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(this)
        recyclerviewlist.layoutManager = linearLayoutManager
        getMyData()


//        Pagination for our lists

        recyclerviewlist.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                if (dy > 0) {
                val visibleItemCount = linearLayoutManager.childCount
                val pastVisibleItem = linearLayoutManager.findFirstCompletelyVisibleItemPosition()
                val total = myAdapter.itemCount

                if (!isLoading) {
                    if ((visibleItemCount + pastVisibleItem) >= total) {
                        page++
                        getMyData()
                    }
                }
//                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })

//        Bottom bar navigation implementation

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

//        Get user's username from firebase database

        database = FirebaseDatabase.getInstance().getReference("users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid).child("usernamesignup")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.value.toString()
                userhomename.text = value
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

//    Retrofit implementation for fetching all coins in the market

    private fun getMyData() {
        isLoading = true
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
            override fun onResponse(
                call: Call<List<CryptoData>>,
                response: Response<List<CryptoData>>
            ) {
                hideProgressBar()
                myAdapter = CryptoListAdapter(baseContext, response.body()!!)
                recyclerviewlist.adapter = myAdapter
                myAdapter.notifyDataSetChanged()
                isLoading = false
            }

            override fun onFailure(call: Call<List<CryptoData>>, t: Throwable) {
                hideProgressBar()
                Toast.makeText(applicationContext, "Check Your Internet Connection!", Toast.LENGTH_LONG).show()
                Log.d("ListActivity", "onFailure:" + t.message)
            }
        })
    }

    private fun hideProgressBar() {
        newLoader.visibility = View.GONE
    }
}