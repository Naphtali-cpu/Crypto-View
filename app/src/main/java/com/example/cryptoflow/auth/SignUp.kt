package com.example.cryptoflow.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.cryptoflow.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUp : AppCompatActivity() {
    private lateinit var databaseReference: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        databaseReference = Firebase.database.reference
        mAuth = Firebase.auth

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val fullName = usernamesignup.text.toString()
        val userName = usernamesignup.text.toString()
        signUpUser(fullName, userName)


        redirectsignin.setOnClickListener {
            val intent = Intent(this, SignIn::class.java)
            startActivity(intent)
        }
    }

    private fun signUpUser(fullName: String, userName: String) {
        signupbutton.setOnClickListener {
            val username = usernamesignup.text.toString()
            val email = signupemail.text.toString()
            val pass = passwordsignup.text.toString()

            if (username.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                usernamesignup.error = "Username is Required!"
                signupemail.error = "Email is Required!"
                passwordsignup.error = "Password is Required!"
            } else {

//                Authenticating user and saving their username to Firebase Database

//    Get current user id
    val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
                val userMap = HashMap<String, Any>()
                userMap["uid"] = currentUserId
                userMap["fullname"] = fullName
                userMap["username"] = userName.toLowerCase()
                userMap["email"] = email
                userMap["bio"] = "Hey! I am using CryptoView!"
                userMap["image"] =
                    "gs://instagram-clone-app-205f9.appspot.com/Default images/profile.png"

                mAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            databaseReference.child("users")
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        databaseReference.child("users")
                                            .child(FirebaseAuth.getInstance().currentUser!!.uid)
                                            .child("usernamesignup").setValue(username)
                                        databaseReference.child("users")
                                            .child(FirebaseAuth.getInstance().currentUser!!.uid)
                                            .child("emailaddress").setValue(email)
                                        databaseReference.child("Follow").child(currentUserId)
                                            .child("Following").child(currentUserId)
                                            .setValue(true)
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        TODO("Not yet implemented")
                                    }
                                })
                            Toast.makeText(
                                applicationContext,
                                "User registered successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(Intent(applicationContext, SignIn::class.java))
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Registration Error: " + task.exception?.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }
}