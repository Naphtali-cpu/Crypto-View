package com.example.cryptoflow.mainui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.example.cryptoflow.R
import com.example.cryptoflow.auth.SignIn
import com.example.cryptoflow.sessions.LoginPref

class SplashScreen : AppCompatActivity() {
    lateinit var session: LoginPref
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        session = LoginPref(this)
        if (session.isLoggedIn()) {
            val intent = Intent(this,ListActivity :: class.java)
            startActivity(intent)
            finish()
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )

            Handler().postDelayed({
                val intent = Intent(this, SignIn::class.java)
                startActivity(intent)
                finish()
            }, 3000)
        }
    }
}