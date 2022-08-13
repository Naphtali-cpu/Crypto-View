package com.example.cryptoflow.mainui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cryptoflow.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_networking.*
import kotlinx.android.synthetic.main.activity_profile.*

class Profile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        BottomSheetBehavior.from(sheet).apply {
            peekHeight = 600
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        val publisher = intent.getStringExtra("PUBLISHER_ID")
        if(publisher!=null) {
            val prefs: SharedPreferences.Editor? =
                getSharedPreferences("PREFS", Context.MODE_PRIVATE)
                    .edit().apply { putString("profileId", publisher); apply() }


        }
        settingsRedirect()
        updateUserAccount()
        privacyRedirectAccount()


    }

    private fun privacyRedirectAccount() {
        privacyProfile.setOnClickListener {
            val intent = Intent(this, Privacy::class.java)
            startActivity(intent)
        }
    }

    private fun updateUserAccount() {
        updateAccount.setOnClickListener {
            val intent = Intent(this, UpdateAccount::class.java)
            startActivity(intent)
        }
    }

    private fun settingsRedirect() {
        settingsSection.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}