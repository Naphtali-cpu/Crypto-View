package com.example.cryptoflow.mainui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cryptoflow.R
import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.android.synthetic.main.activity_networking.*

class Networking : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_networking)
        redirectPostThoughts()
        pickImageFromGallery()
    }

    private fun pickImageFromGallery() {
        TODO("Not yet implemented")
    }

    private fun redirectPostThoughts() {
        backToHome.setOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
            startActivity(intent)
        }
    }
}