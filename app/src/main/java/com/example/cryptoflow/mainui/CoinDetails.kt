package com.example.cryptoflow.mainui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.DashPathEffect
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cryptoflow.R
import com.example.cryptoflow.adapters.CryptoGraphAdapter
import com.example.cryptoflow.api.ModuleItem
import com.example.cryptoflow.data.CoinPrice
import com.example.cryptoflow.data.GraphData
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_coin_details.*
import kotlinx.android.synthetic.main.activity_sign_in.*


class CoinDetails : AppCompatActivity() {

    companion object {
        const val BASE_URL = "https://www.cryptocompare.com/"
    }
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin_details)

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
        fun setChartData(historicalChartModuleData: HistoricalChartModuleData) {
            setupChart(historicalChartModuleData.historicalDataPair!!)
        }

    }

//    Plot data to the chart form the API

    private fun setupChart(dataListPair: Pair<List<GraphData.Data>, GraphData.Data?>) {
        stockChartData.adapter =
            CryptoGraphAdapter(dataListPair.first, dataListPair.second?.open)

        // historicalChartView.fillType=SparkView.FillType.DOWN

        val baseLinePaint = stockChartData.baseLinePaint
        val dashPathEffect = DashPathEffect(floatArrayOf(10.0f, 2.0f), 0f)
        baseLinePaint.pathEffect = dashPathEffect
    }

    data class HistoricalChartModuleData(
        val coinPrice: CoinPrice?,
        val period: String,
        val fromCurrency: String,
        val historicalDataPair: Pair<List<GraphData.Data>, GraphData.Data?>?
    ) : ModuleItem
}