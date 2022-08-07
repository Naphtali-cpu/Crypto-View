package com.example.cryptoflow.mainui

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cryptoflow.R

class Profile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val publisher = intent.getStringExtra("PUBLISHER_ID")
        if(publisher!=null) {
            val prefs: SharedPreferences.Editor? =
                getSharedPreferences("PREFS", Context.MODE_PRIVATE)
                    .edit().apply { putString("profileId", publisher); apply() }


        }
    }
}