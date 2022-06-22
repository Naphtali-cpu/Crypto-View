package com.example.cryptoflow.mainui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cryptoflow.R
import kotlinx.android.synthetic.main.activity_coin_details.*

class CoinDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin_details)

        val coinName = intent.getStringExtra("name")

        textCoin.text = coinName
    }
}