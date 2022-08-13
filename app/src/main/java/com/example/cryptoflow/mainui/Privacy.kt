package com.example.cryptoflow.mainui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.cryptoflow.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_privacy.*

class Privacy : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy)

        reset_button.setOnClickListener {
            val email: String = forgotemail.text.toString().trim { it <= ' ' }
            if (email.isEmpty()) {
                Toast.makeText(this,"Please enter email address", Toast.LENGTH_SHORT).show()
            } else {
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val intent = Intent(this, Profile::class.java)
                            startActivity(intent)
                            Toast.makeText(this,"Email sent successfully to reset your password",
                                Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
            }
        }
    }
}