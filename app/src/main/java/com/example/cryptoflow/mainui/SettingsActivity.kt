package com.example.cryptoflow.mainui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.cryptoflow.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.nav_settings
        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(applicationContext, ListActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_settings -> return@OnNavigationItemSelectedListener true
                R.id.nav_news -> {
                    startActivity(Intent(applicationContext, NewsActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }

            }
            false
        })

        val forward = findViewById<ImageView>(R.id.nextinfo)
        forward.setOnClickListener {
            val intent = Intent(this, AppInfo::class.java)
            startActivity(intent)
        }

    }
}