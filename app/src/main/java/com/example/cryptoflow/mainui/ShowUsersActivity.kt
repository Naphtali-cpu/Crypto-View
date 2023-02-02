package com.example.cryptoflow.mainui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoflow.R
import com.example.cryptoflow.adapters.UserAdapter
import com.example.cryptoflow.data.User
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_show_users.*

class ShowUsersActivity : AppCompatActivity() {

    private lateinit var dbref: DatabaseReference
    private lateinit var userRecyclerview: RecyclerView
    private lateinit var userArrayList: ArrayList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_users)
//        bottomBar()

        userRecyclerview = findViewById(R.id.recycler_view_users)
        userRecyclerview.layoutManager = LinearLayoutManager(this)
        userRecyclerview.setHasFixedSize(true)

        userArrayList = arrayListOf<User>()
        getUserData()
    }

//    private fun bottomBar() {
//        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
//        bottomNavigationView.selectedItemId = R.id.nav_search
//        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
//            when (menuItem.itemId) {
//                R.id.nav_news -> {
//                    startActivity(Intent(applicationContext, NewsActivity::class.java))
//                    overridePendingTransition(0, 0)
//                    return@OnNavigationItemSelectedListener true
//                }
//                R.id.nav_home -> {
//                    startActivity(Intent(applicationContext, ListActivity::class.java))
//                    overridePendingTransition(0, 0)
//                    return@OnNavigationItemSelectedListener true
//                }
//                R.id.nav_search -> return@OnNavigationItemSelectedListener true
//                R.id.nav_network -> {
//                    startActivity(Intent(applicationContext, PostsActivity::class.java))
//                    overridePendingTransition(0, 0)
//                    return@OnNavigationItemSelectedListener true
//                }
//                R.id.nav_profile -> {
//                    startActivity(Intent(applicationContext, Profile::class.java))
//                    overridePendingTransition(0, 0)
//                    return@OnNavigationItemSelectedListener true
//                }
//
//            }
//            false
//        })
//    }

    private fun getUserData() {

        dbref = FirebaseDatabase.getInstance().getReference("Users")

        dbref.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){

                    for (userSnapshot in snapshot.children){


                        val user = userSnapshot.getValue(User::class.java)
                        userArrayList.add(user!!)

                    }

                    userRecyclerview.adapter = UserAdapter(baseContext, userArrayList)


                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })

    }


//    private fun retrieveUser() {
//        val usersSearchRef =
//            FirebaseDatabase.getInstance().reference.child("Users")//table name:Users
//        usersSearchRef.addValueEventListener(object : ValueEventListener {
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(
//                    applicationContext,
//                    "Could not read from Database",
//                    Toast.LENGTH_LONG
//                ).show()
//            }
//
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                if (userSearch.text.toString().equals("")) {
//                    mUser?.clear()
//                    for (snapShot in dataSnapshot.children) {
//                        val user = snapShot.getValue(User::class.java)
//                        if (user != null) {
//                            mUser?.add(user)
//                        }
//                        userAdapter?.notifyDataSetChanged()
//                    }
//                }
//            }
//        })
//    }
//    userSearch.addTextChangedListener(object : TextWatcher {
//        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//
//        }
//
//        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//            if (userSearch.text.toString() == "") {
//
//            } else {
//                recyclerView.visibility = View.GONE
//                retrieveUser()
//                searchUser(p0.toString().toLowerCase())
//            }
//        }
//
//        override fun afterTextChanged(p0: Editable?) {
//
//        }
//
//    })
//
//    private fun searchUser(input: String) {
//
//        val query = FirebaseDatabase.getInstance().reference
//            .child("Users")
//            .orderByChild("username")
//            .startAt(input)
//            .endAt(input + "\uf8ff")
//
//        query.addValueEventListener(object : ValueEventListener {
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(applicationContext, "Data doesn't exist", Toast.LENGTH_LONG).show()
//            }
//
//            override fun onDataChange(datasnapshot: DataSnapshot) {
////                mUser?.clear()
//                if (datasnapshot.exists()) {
//                    userArrayList.clear()
//                    for (snapshot in datasnapshot.children) {
//                        //searching all users
//                        val user = snapshot.getValue(User::class.java)
//                        if (user != null) {
//                            userArrayList.add(user)
//                        }
//                    }
//                    recyclerView.adapter = UserAdapter(baseContext, userArrayList)
////                    userAdapter?.notifyDataSetChanged()
//                } else {
//                    Toast.makeText(applicationContext, "Data doesn't exist", Toast.LENGTH_LONG)
//                        .show()
//                }
//
//            }
//        })
//    }
}