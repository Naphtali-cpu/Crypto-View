package com.example.cryptoflow.mainui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
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

//        Checking size of the marketcap values and replacing with trillion, billion etc

        if (marketCap?.length!! >= 15 || marketCap?.length!! >= 14 || marketCap?.length!! >= 13) {
//            Trillion
            markCap.text = "$ ${marketCap.slice(0..2)} Trillion"
        } else if (marketCap?.length!! >= 12 || marketCap?.length!! >= 11 || marketCap?.length!! >= 10) {
//            Billion
            markCap.text = "$ ${marketCap.slice(0..2)} Billion"
        } else if (marketCap?.length!! >= 9 || marketCap?.length!! >= 8 || marketCap?.length!! >= 7) {
//            Million
            markCap.text = "$ ${marketCap.slice(0..2)} Million"
        } else {
            Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_LONG).show()
        }

//        Chacking the size of volume value, replacing with trillion, billion etc

        if (totalVolume?.length!! >= 15 || totalVolume?.length!! >= 14 || totalVolume?.length!! >= 13) {
//            Trillion
            totalVol.text = "$ ${totalVolume.slice(0..2)} Trillion"
        } else if (totalVolume?.length!! >= 12 || totalVolume?.length!! >= 11 || totalVolume?.length!! >= 10) {
//            Billion
            totalVol.text = "$ ${totalVolume.slice(0..2)} Billion"
        } else if (totalVolume?.length!! >= 9 || totalVolume?.length!! >= 8 || totalVolume?.length!! >= 7) {
//            Million
            totalVol.text = "$ ${totalVolume.slice(0..2)} Million"
        } else {
            Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_LONG).show()
        }

        markRank.text = "# $popularity"
        currPrice.text = "$ $currentPrice"
        Picasso.with(this).load(coinLogo).into(imageCoin)

        back.setOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
            startActivity(intent)
        }
    }
}