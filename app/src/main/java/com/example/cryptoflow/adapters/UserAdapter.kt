package com.example.cryptoflow.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoflow.R
import com.example.cryptoflow.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import android.view.View as AndroidViewView

class UserAdapter(
    private var mContext: Context,
    private var mUser: List<User>,
) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    private val firebaseUser: FirebaseUser? =
        FirebaseAuth.getInstance().currentUser

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.ViewHolder {
        //to make user item available in search item
        val view = LayoutInflater.from(mContext).inflate(R.layout.user_follow_item, parent, false)

        return UserAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mUser.size
    }

    override fun onBindViewHolder(holder: UserAdapter.ViewHolder, position: Int) {
        //to display the user data
        val user = mUser[position]
        holder.userNameTextView.text = user.getUsername()
        Picasso.with(holder.userProfileImage.context).load(user.getImage())
            .placeholder(R.drawable.face)
            .into(holder.userProfileImage)

        checkFollowingStatus(user.getUid(), holder.followButton)

        holder.followButton.setOnClickListener {
            if (holder.followButton.text.toString() == "Follow") {
                firebaseUser?.uid.let { it1 ->
                    FirebaseDatabase.getInstance().reference
                        .child("Follow").child(it1.toString())
                        .child("Following").child(user.getUid())
                        .setValue(true).addOnCompleteListener { task ->
                            if (task.isSuccessful) {

                                firebaseUser?.uid.let { it1 ->
                                    FirebaseDatabase.getInstance().reference
                                        .child("Follow").child(user.getUid())
                                        .child("Followers").child(it1.toString())
                                        .setValue(true).addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                pushNotification(user.getUid())
                                            }
                                        }
                                }
                            }
                        }
                }
            } else {
                if (holder.followButton.text.toString() == "Following") {
                    firebaseUser?.uid.let { it1 ->
                        FirebaseDatabase.getInstance().reference
                            .child("Follow").child(it1.toString())
                            .child("Following").child(user.getUid())
                            .removeValue()
                            .addOnCompleteListener { task -> //reversing following action
                                if (task.isSuccessful) {
                                    firebaseUser?.uid.let { it1 ->
                                        FirebaseDatabase.getInstance().reference
                                            .child("Follow").child(user.getUid())
                                            .child("Followers").child(it1.toString())
                                            .removeValue().addOnCompleteListener { task ->
                                                if (task.isSuccessful) {
                                                }
                                            }
                                    }
                                }
                            }
                    }
                }
            }
        }
    }

    class ViewHolder(@NonNull itemView: AndroidViewView) : RecyclerView.ViewHolder(itemView) {
        var userNameTextView: TextView = itemView.findViewById(R.id.username)
        var userProfileImage: CircleImageView = itemView.findViewById(R.id.userImageProfile)
        var followButton: Button = itemView.findViewById(R.id.btn_follow)
    }

    private fun pushNotification(userid: String) {

        val ref = FirebaseDatabase.getInstance().reference.child("Notification").child(userid)

        val notifyMap = HashMap<String, Any>()
        notifyMap["userid"] = FirebaseAuth.getInstance().currentUser!!.uid
        notifyMap["text"] = "started following you"
        notifyMap["postid"] = ""
        notifyMap["ispost"] = false

        ref.push().setValue(notifyMap)
    }

    private fun checkFollowingStatus(uid: String, followButton: Button) {
        val followingRef = firebaseUser?.uid.let { it1 ->
            FirebaseDatabase.getInstance().reference
                .child("Follow").child(it1.toString())
                .child("Following")
        }

        followingRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(datasnapshot: DataSnapshot) {
                if (datasnapshot.child(uid).exists()) {
                    followButton.text = "Following"
                } else {
                    followButton.text = "Follow"
                }
            }
        })
    }
}