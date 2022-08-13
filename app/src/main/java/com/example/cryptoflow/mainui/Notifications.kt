package com.example.cryptoflow.mainui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoflow.R
import com.example.cryptoflow.adapters.NotificationAdapter
import com.example.cryptoflow.data.Notification
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_notifications.*
import java.util.*
import kotlin.collections.ArrayList

class Notifications : AppCompatActivity() {

    private var notificationAdapter: NotificationAdapter?=null
    private var notificationList:MutableList<Notification>?=null
    private  var firebaseUser: FirebaseUser?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        var recyclerView: RecyclerView?=null
        recyclerView = findViewById(R.id.recyclerview_notification)
        recyclerView.setHasFixedSize(true)
        val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager=linearLayoutManager

        notificationList = ArrayList()
        notificationAdapter = this.let { NotificationAdapter(it,notificationList as ArrayList<Notification>) }
        recyclerView.adapter = notificationAdapter

        readNotification()
    }

    private fun readNotification() {
        val postRef= FirebaseDatabase.getInstance().reference.child("Notification").child(firebaseUser!!.uid)
        postRef.addValueEventListener(object : ValueEventListener
        {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot)
            {
                notificationList?.clear()
                for (snapshot in p0.children)
                {
                    val notification: Notification? = snapshot.getValue(Notification::class.java)
                    notificationList!!.add(notification!!)
                }
                hideProgressBar()
                Collections.reverse(notificationList)
                notificationAdapter!!.notifyDataSetChanged()

            }
        })
    }

    private fun hideProgressBar() {
        notifLoader.visibility = View.GONE
    }
}