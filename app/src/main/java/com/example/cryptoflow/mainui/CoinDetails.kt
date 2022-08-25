package com.example.cryptoflow.mainui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cryptoflow.R
import com.example.cryptoflow.adapters.CryptoGraphAdapter
import com.example.cryptoflow.api.ApiInterface
import com.example.cryptoflow.data.CryptoData
import com.example.cryptoflow.data.cryptodetailmodel.TestDetailData
import com.example.cryptoflow.data.graphmodel.GraphData
import com.example.cryptoflow.data.graphmodel.GraphDataSubList
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_coin_details.*
import kotlinx.android.synthetic.main.activity_list.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.NumberFormat


class CoinDetails : AppCompatActivity() {

    companion object {
        const val BASEGRAPH_URL = "https://api.coingecko.com/api/v3/"
    }

    private lateinit var adapter: CryptoGraphAdapter
    private lateinit var newgraphdata: ArrayList<GraphDataSubList>


    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin_details)

        val id = intent.getStringExtra("id")
        val percent = intent.getStringExtra("percent")
        val coinName = intent.getStringExtra("name")
        val marketCap = intent.getStringExtra("marketcap")
        val lastUpdate = intent.getStringExtra("lastupdate")
        val totalVolume = intent.getStringExtra("totalvolume")
        val currentPrice = intent.getStringExtra("currentprice")
        val popularity = intent.getStringExtra("popularity")
        val coinLogo = intent.getStringExtra("logourl")
        val imageCoin: ImageView = findViewById(R.id.coin)
        getDetailData(id)
        plotGraphData(id)

        textCoin.text = coinName

        if (percent != null) {
            if (percent >= 0.toString()) {
                coinPercent.text = "${percent} %"
                coinPercent.setTextColor(Color.parseColor("#4CAF50"))
            } else {
                coinPercent.text = "${percent} %"
                coinPercent.setTextColor(Color.parseColor("#DF2F2F"))
            }
        }

//        Checking size of the marketcap values and replacing with trillion, billion etc

        if (marketCap != null) {
            if (marketCap.length >= 15 || marketCap.length >= 14 || marketCap.length >= 13) {
                //            Trillion
                markCap.text = "$ ${marketCap.slice(0..2)} Trillion"
            } else if (marketCap.length >= 12 || marketCap.length >= 11 || marketCap.length >= 10) {
                //            Billion
                markCap.text = "$ ${marketCap.slice(0..2)} Billion"
            } else if (marketCap.length >= 9 || marketCap.length >= 8 || marketCap.length >= 7) {
                //            Million
                markCap.text = "$ ${marketCap.slice(0..2)} Million"
            } else {
                Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_LONG).show()
            }
        }

//        Chacking the size of volume value, replacing with trillion, billion etc

        if (totalVolume != null) {
            if (totalVolume.length >= 15 || totalVolume.length >= 14 || totalVolume.length >= 13) {
                //            Trillion
                totalVol.text = "$ ${totalVolume.slice(0..2)} Trillion"
            } else if (totalVolume.length >= 12 || totalVolume.length >= 11 || totalVolume.length >= 10) {
                //            Billion
                totalVol.text = "$ ${totalVolume.slice(0..2)} Billion"
            } else if (totalVolume.length >= 9 || totalVolume.length >= 8 || totalVolume.length >= 7) {
                //            Million
                totalVol.text = "$ ${totalVolume.slice(0..2)} Million"
            } else {
                Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_LONG).show()
            }
        }

        markRank.text = "# $popularity"
        currPrice.text = "$ $currentPrice"

        Picasso.with(this).load(coinLogo).into(imageCoin)

//        back.setOnClickListener {
//            val intent = Intent(this, PostsActivity::class.java)
//            startActivity(intent)
//        }
        rbPeriod1D.setOnClickListener {
            plotGraphData(id)
        }
        oneWeekData(id)
        oneMonthData(id)
        threeMonthData(id)
        oneYearData(id)

    }

    private fun plotGraphData(id: String?) {
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASEGRAPH_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        val covidService = retrofit.create(ApiInterface::class.java)

        covidService.getGraphData(id.toString(), 1)
            .enqueue(object : Callback<ArrayList<GraphDataSubList>> {
                override fun onResponse(
                    call: Call<ArrayList<GraphDataSubList>>,
                    response: Response<ArrayList<GraphDataSubList>>
                ) {
                    Log.i("TAG", "onResponse $response")
                    val nationalData = response.body()
                    if (nationalData == null) {
                        Log.w("TAGTEST", "Did not receive a valid response body")
                        return
                    }
                    setupEventListeners()
                    hideProgressBar()
                    newgraphdata = nationalData.reversed() as ArrayList<GraphDataSubList>
                    Log.i("TAG", "Update graph with national data")
                    updateDisplayWithData(newgraphdata)
//                addChartScrubListener()
                }

                override fun onFailure(call: Call<ArrayList<GraphDataSubList>>, t: Throwable) {
                    Log.e("TAG", "onFailure $t")
                    hideProgressBar()
                }

            })
    }

//    Graph Data for one week

    private fun oneWeekData(id: String?) {
        rbPeriod1W.setOnClickListener {
            val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()
            val retrofit = Retrofit.Builder()
                .baseUrl(BASEGRAPH_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
            val covidService = retrofit.create(ApiInterface::class.java)

            covidService.getGraphData(id.toString(), 7)
                .enqueue(object : Callback<ArrayList<GraphDataSubList>> {
                    override fun onResponse(
                        call: Call<ArrayList<GraphDataSubList>>,
                        response: Response<ArrayList<GraphDataSubList>>
                    ) {
                        Log.i("TAG", "onResponse $response")
                        val nationalData = response.body()
                        if (nationalData == null) {
                            Log.w("TAGTEST", "Did not receive a valid response body")
                            return
                        }
                        setupEventListeners()
                        hideProgressBar()
                        newgraphdata = nationalData.reversed() as ArrayList<GraphDataSubList>
                        Log.i("TAG", "Update graph with national data")
                        updateDisplayWithData(newgraphdata)
//                addChartScrubListener()
                    }

                    override fun onFailure(call: Call<ArrayList<GraphDataSubList>>, t: Throwable) {
                        Log.e("TAG", "onFailure $t")
                    }

                })
        }
    }

    //    One Month Data

    private fun oneMonthData(id: String?) {

        rbPeriod1M.setOnClickListener {
            val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()
            val retrofit = Retrofit.Builder()
                .baseUrl(BASEGRAPH_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
            val covidService = retrofit.create(ApiInterface::class.java)

            covidService.getGraphData(id.toString(), 30)
                .enqueue(object : Callback<ArrayList<GraphDataSubList>> {
                    override fun onResponse(
                        call: Call<ArrayList<GraphDataSubList>>,
                        response: Response<ArrayList<GraphDataSubList>>
                    ) {
                        Log.i("TAG", "onResponse $response")
                        val nationalData = response.body()
                        if (nationalData == null) {
                            Log.w("TAGTEST", "Did not receive a valid response body")
                            return
                        }
                        setupEventListeners()
                        hideProgressBar()
                        newgraphdata = nationalData.reversed() as ArrayList<GraphDataSubList>
                        Log.i("TAG", "Update graph with national data")
                        updateDisplayWithData(newgraphdata)
//                addChartScrubListener()
                    }

                    override fun onFailure(call: Call<ArrayList<GraphDataSubList>>, t: Throwable) {
                        Log.e("TAG", "onFailure $t")
                    }

                })
        }

    }

    //    Three Month Data

    private fun threeMonthData(id: String?) {

        rbPeriod3M.setOnClickListener {
            val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()
            val retrofit = Retrofit.Builder()
                .baseUrl(BASEGRAPH_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
            val covidService = retrofit.create(ApiInterface::class.java)

            covidService.getGraphData(id.toString(), 90)
                .enqueue(object : Callback<ArrayList<GraphDataSubList>> {
                    override fun onResponse(
                        call: Call<ArrayList<GraphDataSubList>>,
                        response: Response<ArrayList<GraphDataSubList>>
                    ) {
                        Log.i("TAG", "onResponse $response")
                        val nationalData = response.body()
                        if (nationalData == null) {
                            Log.w("TAGTEST", "Did not receive a valid response body")
                            return
                        }
                        setupEventListeners()
                        hideProgressBar()
                        newgraphdata = nationalData.reversed() as ArrayList<GraphDataSubList>
                        Log.i("TAG", "Update graph with national data")
                        updateDisplayWithData(newgraphdata)
//                addChartScrubListener()
                    }

                    override fun onFailure(call: Call<ArrayList<GraphDataSubList>>, t: Throwable) {
                        Log.e("TAG", "onFailure $t")
                    }

                })
        }

    }

    //    One Year Graph Data

    private fun oneYearData(id: String?) {
        rbPeriod1Y.setOnClickListener {
            val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()
            val retrofit = Retrofit.Builder()
                .baseUrl(BASEGRAPH_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
            val covidService = retrofit.create(ApiInterface::class.java)

            covidService.getGraphData(id.toString(), 365)
                .enqueue(object : Callback<ArrayList<GraphDataSubList>> {
                    override fun onResponse(
                        call: Call<ArrayList<GraphDataSubList>>,
                        response: Response<ArrayList<GraphDataSubList>>
                    ) {
                        Log.i("TAG", "onResponse $response")
                        val nationalData = response.body()
                        if (nationalData == null) {
                            Log.w("TAGTEST", "Did not receive a valid response body")
                            return
                        }
                        setupEventListeners()
                        hideProgressBar()
                        newgraphdata = nationalData.reversed() as ArrayList<GraphDataSubList>
                        Log.i("TAG", "Update graph with national data")
                        updateDisplayWithData(newgraphdata)
//                addChartScrubListener()
                    }

                    override fun onFailure(call: Call<ArrayList<GraphDataSubList>>, t: Throwable) {
                        Log.e("TAG", "onFailure $t")
                    }

                })
        }
    }

//    private fun addChartScrubListener() {
//        TODO("Not yet implemented")
//    }

    private fun getDetailData(id: String?) {
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

        val retrofitData = retrofitBuilder.getCurrentData(id.toString()).enqueue(object :
            Callback<TestDetailData> {
            override fun onResponse(
                call: Call<TestDetailData>,
                response: Response<TestDetailData>
            ) {
                if (response.isSuccessful) {
                    val destination = response.body()
                    destination?.let {
                        aboutCoin.setText(destination.description.en)
                    }
                } else {

                }

            }

            override fun onFailure(call: Call<TestDetailData>, t: Throwable) {
                Toast.makeText(
                    applicationContext,
                    "Something went wrong on our side",
                    Toast.LENGTH_LONG
                ).show()
                Log.d("ListActivity", "onFailure:" + t.message)
            }
        })
    }

    private fun updateDisplayWithData(newgraphdata: ArrayList<GraphDataSubList>) {
        adapter = CryptoGraphAdapter(newgraphdata)
        stockChartData.adapter = adapter
    }

    private fun setupEventListeners() {
        stockChartData.isScrubEnabled = true
        stockChartData.setScrubListener { itemData ->
            if (itemData is GraphDataSubList) {
//                updateScrubData(itemData)
            }
        }
    }

//    private fun updateScrubData(scrubData: GraphDataSubList) {
//        val currentPrice = ArrayList<GraphDataSubList>()
//        currPrice.text = NumberFormat.getInstance().format(currentPrice[1][4].toFloat())
//    }

//    private fun updateInfoOnScrub(dataSubList: GraphDataSubList) {
//        currPrice.text = NumberFormat.getInstance().format(adapter)
//    }

    private fun hideProgressBar() {
        graphLoader.visibility = View.GONE
    }

}