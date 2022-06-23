package com.example.cryptoflow.mainui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.cryptoflow.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_coin_details.*
import kotlinx.android.synthetic.main.activity_sign_in.*


class CoinDetails : AppCompatActivity() {
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
        markCap.text = "$ $marketCap"
        totalVol.text = "$ $totalVolume"
        markRank.text = "# $popularity"
        currPrice.text = "$ $currentPrice"
        Picasso.with(this).load(coinLogo).into(imageCoin)

        back.setOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
            startActivity(intent)
        }
    }
}