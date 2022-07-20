package com.example.cryptoflow.mainui

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.DashPathEffect
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cryptoflow.R
import com.example.cryptoflow.adapters.CryptoGraphAdapter
import com.example.cryptoflow.api.ApiInterface
import com.example.cryptoflow.api.ModuleItem
import com.example.cryptoflow.data.CoinPrice
import com.example.cryptoflow.data.CryptoData
import com.example.cryptoflow.data.GraphData
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_coin_details.*
import kotlinx.android.synthetic.main.activity_sign_in.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class CoinDetails : AppCompatActivity() {

    companion object {
        const val BASEGRAPH_URL = "https://api.coingecko.com/api/v3/"
    }

    private lateinit var adapter: CryptoGraphAdapter
    private lateinit var newgraphdata: List<CryptoData>


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin_details)

        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASEGRAPH_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        val covidService = retrofit.create(ApiInterface::class.java)

        covidService.getCrypto().enqueue(object : Callback<List<CryptoData>> {
            override fun onResponse(
                call: Call<List<CryptoData>>,
                response: Response<List<CryptoData>>
            ) {
                Log.i(TAG, "onResponse $response")
                val nationalData = response.body()
                if (nationalData == null) {
                    Log.w("TAGTEST", "Did not receive a valid response body")
                    return
                }
                setupEventListeners()

                newgraphdata = nationalData.reversed()
                Log.i(TAG, "Update graph with national data")
                updateDisplayWithData(newgraphdata)
            }

            override fun onFailure(call: Call<List<CryptoData>>, t: Throwable) {
                Log.e(TAG, "onFailure $t")
            }

        })
        val coinName = intent.getStringExtra("name")
        val marketCap = intent.getStringExtra("marketcap")
        val lastUpdate = intent.getStringExtra("lastupdate")
        val totalVolume = intent.getStringExtra("totalvolume")
        val currentPrice = intent.getStringExtra("currentprice")
        val popularity = intent.getStringExtra("popularity")
        val coinLogo = intent.getStringExtra("logourl")
        val imageCoin: ImageView = findViewById(R.id.coin)

        textCoin.text = coinName

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

        back.setOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
            startActivity(intent)
        }

    }

    private fun updateDisplayWithData(newgraphdata: List<CryptoData>) {
        adapter = CryptoGraphAdapter(newgraphdata)
        stockChartData.adapter = adapter
    }

    private fun setupEventListeners() {
        stockChartData.isScrubEnabled = true
    }

}