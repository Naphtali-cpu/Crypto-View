package com.example.cryptoflow.mainui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoflow.R
import com.example.cryptoflow.adapters.CryptoListAdapter
import com.example.cryptoflow.adapters.PostAdapter
import com.example.cryptoflow.api.ApiInterface
import com.example.cryptoflow.data.CryptoData
import com.example.cryptoflow.data.Post
import com.example.cryptoflow.sessions.LoginPref
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.android.synthetic.main.activity_list.recycler_view_home
import kotlinx.android.synthetic.main.activity_news.*
import kotlinx.android.synthetic.main.activity_posts.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


const val HOME_URL = "https://api.coingecko.com/api/v3/"

class ListActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    lateinit var session: LoginPref
    lateinit var myAdapter: CryptoListAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    private var recyclerView: RecyclerView? = null
    private lateinit var dbref : DatabaseReference
    private lateinit var userRecyclerview : RecyclerView
    private lateinit var userArrayList : ArrayList<Post>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        initRecycler()
        getMyData()
        userNameFirebase()
        bottomBar()
        redirectPostThoughts()
        recyclerPostInilialise()
        retrieveUserPosts()
        redirectNotif()
    }

    private fun redirectNotif() {
        notifIcon.setOnClickListener {
            val intent = Intent(this, Notifications::class.java)
            startActivity(intent)
        }
    }

    private fun retrieveUserPosts() {
        dbref = FirebaseDatabase.getInstance().getReference("Posts")

        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){

                    for (userSnapshot in snapshot.children){
                        val user = userSnapshot.getValue(Post::class.java)
                        userArrayList.add(user!!)
                    }
                    userRecyclerview.adapter = PostAdapter(baseContext, userArrayList)
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_LONG).show()
                Log.d("DBERR", error.toString())
            }


        })
    }

    private fun recyclerPostInilialise() {
        recycler_view_home.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(this)
        recycler_view_home.layoutManager = linearLayoutManager

        recycler_view_home.addItemDecoration(
            DividerItemDecoration(
                baseContext,
                linearLayoutManager.orientation
            )
        )

        userRecyclerview = findViewById(R.id.recycler_view_home)
        userRecyclerview.layoutManager = LinearLayoutManager(this)
        userRecyclerview.setHasFixedSize(true)

        userArrayList = arrayListOf<Post>()
        recyclerView = findViewById(R.id.recycler_view_home)
    }


    private fun redirectPostThoughts() {
        link_to_post.setOnClickListener {
            val intent = Intent(this, Networking::class.java)
            startActivity(intent)
        }
    }

//    Initialize recyclerview

    private fun initRecycler() {

        homerecyclerviewlist.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(this)
        homerecyclerviewlist.layoutManager = linearLayoutManager

    }

    //        Bottom bar navigation implementation

    private fun bottomBar() {

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.nav_home
        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_news -> {
                    startActivity(Intent(applicationContext, NewsActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_home -> return@OnNavigationItemSelectedListener true
                R.id.nav_network -> {
                    startActivity(Intent(applicationContext, PostsActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(applicationContext, Profile::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }

            }
            false
        })
    }

    //        Get user's username from firebase database
    private fun userNameFirebase() {

        database = FirebaseDatabase.getInstance().getReference("Users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid).child("fullname")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.value.toString()
                userhomename.text = value
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

//    Retrofit implementation for fetching all coins in the market

    private fun getMyData() {
        val okhttpHttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder().addInterceptor(
            okhttpHttpLoggingInterceptor
        )

        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(HOME_URL)
            .client(okHttpClient.build())
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getCrypto(2, 1, "market_cap_desc").enqueue(object :
            Callback<List<CryptoData>> {
            override fun onResponse(
                call: Call<List<CryptoData>>,
                response: Response<List<CryptoData>>
            ) {
                hideProgressBar()
                myAdapter = CryptoListAdapter(baseContext, response.body()!!)
                homerecyclerviewlist.adapter = myAdapter
                myAdapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<List<CryptoData>>, t: Throwable) {
                hideProgressBar()
                Toast.makeText(applicationContext, "Check Your Internet Connection!", Toast.LENGTH_LONG).show()
                Log.d("ListActivity", "onFailure:" + t.message)
            }
        })
    }

    private fun hideProgressBar() {
        newLoader.visibility = View.GONE
    }
}