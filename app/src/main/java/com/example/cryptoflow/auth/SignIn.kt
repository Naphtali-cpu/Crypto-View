package com.example.cryptoflow.auth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.cryptoflow.R
import com.example.cryptoflow.mainui.ListActivity
import com.example.cryptoflow.sessions.LoginPref
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignIn : AppCompatActivity() {

    companion object {
        @JvmStatic
        fun buildLaunchIntent(context: Context): Intent {
            return Intent(context, SignIn::class.java)
        }
    }

    lateinit var session: LoginPref
    private lateinit var databaseReference: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        session = LoginPref(this)
        databaseReference = Firebase.database.reference
        mAuth = Firebase.auth

        if (session.isLoggedIn()) {
            val intent = Intent(this,ListActivity :: class.java)
            startActivity(intent)
            finish()
        } else {
            logInUser()
        }

    }

    private fun logInUser() {
        signinbutton.setOnClickListener {
            val email = signinemail.text.toString()
            val password = signinpassword.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                signinemail.error = "Email is Required!"
                signinpassword.error = "Password is Required"
            } else {
//                Saving the users data to session
                session.createLoginSession(email, password)

//                Login user using email and password only

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(applicationContext, "Logged In Successfully", Toast.LENGTH_SHORT)
                            .show()
                        startActivity(Intent(applicationContext, ListActivity::class.java))
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Log in Error: " + task.exception?.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        redirectsignup.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }
    }
}