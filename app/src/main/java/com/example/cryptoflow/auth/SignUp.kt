package com.example.cryptoflow.auth

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
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

        signupbutton.setOnClickListener {
            createAccount()
        }


        redirectsignin.setOnClickListener {
            val intent = Intent(this, SignIn::class.java)
            startActivity(intent)
        }
    }

    private fun createAccount() {
        val fullName=signup_fullname.text.toString()
        val userName=signup_username.text.toString()
        val email=signup_email.text.toString()
        val password=signup_password.text.toString()


        when{
            TextUtils.isEmpty(fullName)-> Toast.makeText(this, "Name is required", Toast.LENGTH_SHORT).show()
            TextUtils.isEmpty(userName)-> Toast.makeText(this, "Username is required", Toast.LENGTH_SHORT).show()
            TextUtils.isEmpty(email)-> Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show()
            TextUtils.isEmpty(password)-> Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show()

            else->
            {

                val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
                mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener { task ->
                        if(task.isSuccessful)
                        {
                            saveUserInfo(fullName,userName,email)
                        }
                        else
                        {
                            val message=task.exception!!.toString()
                            Toast.makeText(this,"Error : $message", Toast.LENGTH_LONG).show()
                            mAuth.signOut()
                        }
                    }
            }
        }
    }

    private fun saveUserInfo(fullName: String, userName: String, email: String) {
        val currentUserId=FirebaseAuth.getInstance().currentUser!!.uid
        val userRef : DatabaseReference=FirebaseDatabase.getInstance().reference.child("Users")

        val userMap=HashMap<String,Any>()
        userMap["uid"]=currentUserId
        userMap["fullname"]=fullName
        userMap["username"]=userName.toLowerCase()
        userMap["email"]=email
        userMap["bio"]="Hey! I am using InstaApp"
        userMap["image"]="gs://instagram-clone-app-205f9.appspot.com/Default images/profile.png"


        userRef.child(currentUserId).setValue(userMap)
            .addOnCompleteListener {task ->
                if(task.isSuccessful)
                {
                    Toast.makeText(this,"Account has been created",Toast.LENGTH_SHORT).show()


                    FirebaseDatabase.getInstance().reference
                        .child("Follow").child(currentUserId)
                        .child("Following").child(currentUserId)
                        .setValue(true)


                    val intent=Intent(this@SignUp,SignIn::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }
                else
                {
                    val message=task.exception!!.toString()
                    Toast.makeText(this,"Error : $message", Toast.LENGTH_LONG).show()
                    FirebaseAuth.getInstance().signOut()
                }
            }
    }

}