package com.example.cryptoflow.mainui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.cardview.widget.CardView
import com.example.cryptoflow.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        nextinfo.setOnClickListener {
            val intent = Intent(this, AppInfo::class.java)
            startActivity(intent)
        }

        shareApp.setOnClickListener {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(
                    Intent.EXTRA_TEXT,
                    "Track 500 + of crypto currencies on 150+ exchanges completely free and secure. You can also share your views or advice on crypto's today market! " +
                            "\n Download Crypto View at: https://github.com/Naphtali-cpu/Crypto-View"
                )
                type = "text/plain"
            }
            startActivity(sendIntent)

        }

    }
}