package com.example.cryptoflow.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoflow.R
import com.example.cryptoflow.data.Post
import com.example.cryptoflow.data.User
import com.example.cryptoflow.mainui.AddCommentActivity
import com.example.cryptoflow.mainui.Profile
import com.example.cryptoflow.mainui.ShowUsersActivity

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.cryptolist.view.*
import java.lang.reflect.Array.get


class PostAdapter(private val mContext:Context, private  val mPost:List<Post>):RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    private var firebaseUser:FirebaseUser?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(mContext).inflate(R.layout.posts_layout,parent,false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
       return  mPost.size
    }

    //code for events
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        firebaseUser= FirebaseAuth.getInstance().currentUser
        val post = mPost[position]
        val postid = post.getPostId()

        Picasso.with(holder.postImage.context).load(post.getPostImage()).into(holder.postImage)
//        Picasso.get().load(post.getPostImage()).into(holder.postImage)
        holder.caption.text=post.getCaption()
        publisherInfo(holder.profileImage, holder.username, post.getPublisher())
        isLiked(post.getPostId(), holder.likeButton, holder.postImage)
        getCountofLikes(post.getPostId(), holder.likes)
        getComments(post.getPostId(), holder.comments)


        holder.profileImage.setOnClickListener {

            val intent = Intent(mContext, Profile::class.java).apply {
                putExtra("PUBLISHER_ID", post.getPublisher())
            }
            mContext.startActivity(intent)
        }

        holder.username.setOnClickListener {

            val intent = Intent(mContext, Profile::class.java).apply {
                putExtra("PUBLISHER_ID", post.getPublisher())
            }
            mContext.startActivity(intent)
        }

        holder.postImage.setOnClickListener {
            if (holder.postImage.tag.toString() == "like") {
                FirebaseDatabase.getInstance().reference.child("Likes").child(post.getPostId())
                    .child(firebaseUser!!.uid)
                    .setValue(true)
            } else {
                FirebaseDatabase.getInstance().reference.child("Likes").child(post.getPostId())
                    .child(firebaseUser!!.uid)
                    .removeValue()
            }

        }

        holder.likeButton.setOnClickListener{
            if (holder.likeButton.tag.toString()=="like")
            {
                FirebaseDatabase.getInstance().reference.child("Likes").child(post.getPostId())
                    .child(firebaseUser!!.uid)
                    .setValue(true)
                pushNotification(post.getPostId(),post.getPublisher())
            }
            else
            {
                FirebaseDatabase.getInstance().reference.child("Likes").child(post.getPostId())
                    .child(firebaseUser!!.uid)
                    .removeValue()
            }
        }

        holder.comments.setOnClickListener {

            val intent = Intent(mContext, AddCommentActivity::class.java).apply {
                putExtra("POST_ID",postid)
            }
            mContext.startActivity(intent)
        }

        holder.likes.setOnClickListener {
            val intent = Intent(mContext, ShowUsersActivity::class.java)
            intent.putExtra("id",post.getPostId())
            intent.putExtra("title","likes")
            mContext.startActivity(intent)
        }

        holder.commentButton.setOnClickListener {

            val intent = Intent(mContext,AddCommentActivity::class.java).apply {
                putExtra("POST_ID",postid)
            }
            mContext.startActivity(intent)
        }

    }

    inner class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var profileImage:CircleImageView
        var postImage:ImageView
        var likeButton:ImageView
        var commentButton:ImageView
        var likes:TextView
        var comments:TextView
        var username:TextView
        var caption:TextView


        init {
            profileImage=itemView.findViewById(R.id.publisher_profile_image_post)
            postImage=itemView.findViewById(R.id.post_image_home)
            likeButton=itemView.findViewById(R.id.post_image_like_btn)
            commentButton=itemView.findViewById(R.id.post_image_comment_btn)
            likes=itemView.findViewById(R.id.likes)
            comments=itemView.findViewById(R.id.comments)
            username=itemView.findViewById(R.id.publisher_user_name_post)
            caption=itemView.findViewById(R.id.caption)

        }

    }

    private fun getComments(postid:String, comment:TextView) {

        val commentRef=FirebaseDatabase.getInstance().reference.child("Comment").child(postid)

        commentRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(datasnapshot: DataSnapshot) {
                comment.text = datasnapshot.childrenCount.toString()
            }
        })
    }

    private fun pushNotification(postid:String, userid:String) {

        val ref = FirebaseDatabase.getInstance().reference.child("Notification").child(userid)

        val notifyMap = HashMap<String, Any>()
        notifyMap["userid"] = FirebaseAuth.getInstance().currentUser!!.uid
        notifyMap["text"] = "♥liked your post♥"
        notifyMap["postid"] = postid
        notifyMap["ispost"] = true

        ref.push().setValue(notifyMap)
    }


    private fun isLiked(postid:String,imageView: ImageView,postedImg:ImageView) {

        firebaseUser=FirebaseAuth.getInstance().currentUser
        val postRef=FirebaseDatabase.getInstance().reference.child("Likes").child(postid)

        postRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(datasnapshot: DataSnapshot) {
                if (datasnapshot.child(firebaseUser!!.uid).exists()) {
                    imageView.setImageResource(R.drawable.likedbutton)
                    postedImg.tag =" liked"
                    imageView.tag = "liked"
                }
                else {
                    imageView.setImageResource(R.drawable.initialike)
                    postedImg.tag = "like"
                    imageView.tag = "like"
                }
            }
        })
    }

    private fun getCountofLikes(postid:String,likesNo: TextView) {

        val postRef=FirebaseDatabase.getInstance().reference.child("Likes").child(postid)

        postRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                }

            override fun onDataChange(datasnapshot: DataSnapshot) {
                likesNo.text = datasnapshot.childrenCount.toString()
            }
        })
    }

    private fun publisherInfo(profileImage: CircleImageView, username: TextView,  publisherID: String) {

        val userRef=FirebaseDatabase.getInstance().reference.child("Users").child(publisherID)
        userRef.addValueEventListener(object :ValueEventListener
        {
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists())
                {
                    val user = snapshot.getValue<User>(User::class.java)
                    Picasso.with(profileImage.context).load(user!!.getImage()).placeholder(R.drawable.profile).into(profileImage)
                    username.text =(user.getUsername())

                }
            }

        })
    }

}