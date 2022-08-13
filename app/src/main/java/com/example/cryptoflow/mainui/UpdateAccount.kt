package com.example.cryptoflow.mainui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.cryptoflow.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_update_account.*

class UpdateAccount : AppCompatActivity() {
    private lateinit var database : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_account)

        updatebutton.setOnClickListener {
            val prefUserName = prefUsername.text.toString()

            updateData(prefUserName)
        }
    }

    private fun updateData(prefUserName: String) {
        database = FirebaseDatabase.getInstance().getReference("Users")

        val user = mapOf<String, String>(
            "username" to prefUserName
        )

        database.child(FirebaseAuth.getInstance().currentUser!!.uid).updateChildren(user).addOnSuccessListener {
            prefUsername.text?.clear()
            Toast.makeText(this,"Username Updated Successfully!", Toast.LENGTH_SHORT).show()
        }
    }
}