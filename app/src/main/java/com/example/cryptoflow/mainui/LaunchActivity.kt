package com.example.cryptoflow.mainui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cryptoflow.R
import com.example.cryptoflow.adapters.LauncherViewPager
import com.example.cryptoflow.auth.SignIn
import com.example.cryptoflow.auth.SignUp
import com.example.cryptoflow.sessions.PreferenceManager
import kotlinx.android.synthetic.main.activity_launch.*

class LaunchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

//        Determine if this is first time, if yes then show the onboarding screens
        if (!PreferenceManager.getPreference(this, PreferenceManager.IS_LAUNCH_FTU_SHOWN, false)) {
            initializeUI()
        } else {
            startActivity(SignIn.buildLaunchIntent(this))
            finish()
        }


    }

//    private fun onBoardingFinished(): Boolean {
//        val sharedPref = this.getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
//        return sharedPref.getBoolean("Finished", false)
//    }

    private fun initializeUI() {
        viewPager.adapter = LauncherViewPager(supportFragmentManager)
        dots_indicator.attachTo(viewPager)
    }
}