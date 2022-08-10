package com.example.cryptoflow.mainui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoflow.R
import com.example.cryptoflow.adapters.CommentAdapter
import com.example.cryptoflow.data.Comment
import com.example.cryptoflow.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_add_comment.*
import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.android.synthetic.main.activity_posts.*

class AddCommentActivity : AppCompatActivity() {

    private var firebaseUser: FirebaseUser? = null
    private var commentAdapter: CommentAdapter? = null
    private var commentList: MutableList<Comment>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_comment)

        var recyclerView: RecyclerView? = null
        recyclerView = findViewById(R.id.recyclerview_comments)
        recyclerView.setHasFixedSize(true)
        val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager

        recyclerView.addItemDecoration(
            DividerItemDecoration(
                baseContext,
                linearLayoutManager.orientation
            )
        )

        commentList = ArrayList()
        commentAdapter = this.let { CommentAdapter(it, commentList as ArrayList<Comment>) }
        recyclerView.adapter = commentAdapter


        firebaseUser = FirebaseAuth.getInstance().currentUser

        val add_comment = findViewById<EditText>(R.id.add_comment)
        val post_comment = findViewById<ImageView>(R.id.post_comment)
        val postid = intent.getStringExtra("POST_ID")

        readComments(postid!!)

        post_comment.setOnClickListener {
            if (add_comment.text.toString().equals("")) {
                Toast.makeText(this, "You can't send an empty comment", Toast.LENGTH_SHORT).show()
            } else {
                postComment(postid!!)
            }
        }
    }

    private fun postComment(postid: String) {
        val commentRef: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child("Comment").child(postid)

        val commentMap = HashMap<String, Any>()
        commentMap["publisher"] = firebaseUser!!.uid
        commentMap["comment"] = add_comment.text.toString()

        commentRef.push().setValue(commentMap)
        pushNotification(postid)
        add_comment.setText("")
        Toast.makeText(this, "posted!!", Toast.LENGTH_LONG).show()
    }

    private fun pushNotification(postid: String) {
        val ref =
            FirebaseDatabase.getInstance().reference.child("Notification").child(firebaseUser!!.uid)

        val notifyMap = HashMap<String, Any>()
        notifyMap["userid"] = FirebaseAuth.getInstance().currentUser!!.uid
        notifyMap["text"] = "commented :" + add_comment.text.toString()
        notifyMap["postid"] = postid
        notifyMap["ispost"] = true

        ref.push().setValue(notifyMap)
    }

    private fun readComments(postid: String) {
        val ref: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child("Comment").child(postid)

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {

                commentList?.clear()
                for (snapshot in p0.children) {
                    val cmnt: Comment? = snapshot.getValue(Comment::class.java)
                    commentList!!.add(cmnt!!)
                }
                hideProgressBar()
                commentAdapter!!.notifyDataSetChanged()
            }
        })
    }

    private fun hideProgressBar() {
        commentsLoader.visibility = View.GONE
    }
}