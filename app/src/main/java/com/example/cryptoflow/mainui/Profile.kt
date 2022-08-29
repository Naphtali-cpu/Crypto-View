package com.example.cryptoflow.mainui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.cryptoflow.R
import com.example.cryptoflow.auth.SignIn
import com.example.cryptoflow.sessions.LoginPref
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_networking.*
import kotlinx.android.synthetic.main.activity_profile.*

class Profile : AppCompatActivity() {
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var database : DatabaseReference

    lateinit var session: LoginPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        session = LoginPref(this)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        getUserInfo()
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

        session.checkLogin()

        logOut.setOnClickListener {
            logOutLoader.visibility = View.VISIBLE
            Toast.makeText(this@Profile, "Logging you out...", Toast.LENGTH_LONG).show()
            session.logoutUser()
            val intent = Intent(this, SignIn::class.java)
            startActivity(intent)
        }

    }

    private fun getUserInfo() {
//        Get Username
        database = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().currentUser!!.uid).child("username")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.value.toString()
                Log.d("TAG", "Value is: " + value)
                user_name.text = value
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_LONG).show()
            }

        })
//        Get User Profile Image
        database = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().currentUser!!.uid).child("image")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val imgValue = snapshot.value.toString()
                Log.d("TAG", "Image Value is: " + imgValue)
                Picasso.with(this@Profile).load(imgValue).into(profileImage)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_LONG).show()
            }

        })
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