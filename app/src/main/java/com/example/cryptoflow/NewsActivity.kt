package com.example.cryptoflow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_list.progressBar
import kotlinx.android.synthetic.main.activity_news.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL2 = "https://api.polygon.io/v2/"


class NewsActivity : AppCompatActivity() {
    lateinit var myAdapter: NewsAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        recyclerviewnews.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(this)
        recyclerviewnews.layoutManager = linearLayoutManager
        getMyData()
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
            .baseUrl(BASE_URL2)
            .client(okHttpClient.build())
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getNews().enqueue(object :
            Callback<List<NewsData>> {
            override fun onResponse(call: Call<List<NewsData>>, response: Response<List<NewsData>>) {
//                hideProgressBar()
                if(response?.body() != null) {
                    myAdapter = NewsAdapter(baseContext, response.body()!!)
                    recyclerviewnews.adapter = myAdapter
                    myAdapter.notifyDataSetChanged()

                }
            }

            override fun onFailure(call: Call<List<NewsData>>, t: Throwable) {
//                hideProgressBar()
                Log.d("NewsActivity", "onFailure:" + t.message)
            }
        })
    }
//    private fun hideProgressBar() {
//        progressBar.setVisibility(View.GONE)
//    }
}