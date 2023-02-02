package com.example.cryptoflow.mainui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoflow.R
import com.example.cryptoflow.adapters.CryptoGraphAdapter
import com.example.cryptoflow.adapters.CryptoListAdapter
import com.example.cryptoflow.adapters.PostAdapter
import com.example.cryptoflow.api.ApiInterface
import com.example.cryptoflow.data.CryptoData
import com.example.cryptoflow.data.Post
import com.example.cryptoflow.data.graphmodel.GraphDataSubList
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
import java.util.ArrayList

const val BASE_URL = "https://api.coingecko.com/api/v3/"

class PostsActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    lateinit var myAdapter: CryptoListAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var linearLayoutManagerAsc: LinearLayoutManager


    var list: MutableList<CryptoData> = mutableListOf()
    var visibleItemCount: Int = 0
    var pastVisibleItemCount: Int = 0
    var totalItemCount: Int = 0
    var loading: Boolean = false
    var pageId: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts)
        recyclerviewlist.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(this)
        recyclerviewlist.layoutManager = linearLayoutManager

        searchCoinImpl()
        getMyData(pageId)
        bottomBar()



    }

    private fun searchCoinImpl() {
        coin_search.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filter(newText)
                return false
            }


        })
    }

    private fun filter(newText: String) {
        val filteredList: java.util.ArrayList<CryptoData> = ArrayList()
        for (item in list) {
            if (item.name.toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(item)
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(this, "Coin not found!", Toast.LENGTH_SHORT).show()
        } else {
            myAdapter.filterList(filteredList)
        }
    }


    private fun getMyData(pageId: Int) {
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

        val retrofitData =
            retrofitBuilder.getCrypto(100, pageId, "market_cap_desc").enqueue(object :
                Callback<List<CryptoData>> {
                override fun onResponse(
                    call: Call<List<CryptoData>>,
                    response: Response<List<CryptoData>>
                ) {
                    hideShimmerEffect()
                    loading = true
                    recyclerviewlist.visibility = View.VISIBLE
                    setUpAdapter(response.body())
                }

                override fun onFailure(call: Call<List<CryptoData>>, t: Throwable) {
                    hideShimmerEffect()
                    Log.d("ListActivity", "onFailure:" + t.message)
                }
            })
    }

    private fun setUpAdapter(body: List<CryptoData>?) {
        if (list.size == 0) {
            list = body as MutableList<CryptoData>
            myAdapter = CryptoListAdapter(baseContext, list)
            recyclerviewlist.adapter = myAdapter
        } else {
            var currentPosition =
                (recyclerviewlist.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            list.addAll(body!!)
            myAdapter.notifyDataSetChanged()
            recyclerviewlist.scrollToPosition(currentPosition)
        }
        recyclerviewlist.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    visibleItemCount = linearLayoutManager.childCount
                    totalItemCount = linearLayoutManager.itemCount
                    pastVisibleItemCount =
                        (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    if (loading) {
                        if ((visibleItemCount + pastVisibleItemCount) >= totalItemCount) {
                            loading = false
                            pageId++
                            getMyData(pageId)
                        }
                    }
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

                super.onScrollStateChanged(recyclerView, newState)
            }
        })
    }

    private fun hideShimmerEffect() {
        cryptoListShimmer.visibility = View.GONE
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