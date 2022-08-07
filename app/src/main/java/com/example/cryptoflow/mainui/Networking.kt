package com.example.cryptoflow.mainui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.example.cryptoflow.R
import com.example.cryptoflow.imglib.ImagePicker
import com.example.cryptoflow.imglib.ImageResult
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.android.synthetic.main.activity_networking.*

class Networking : AppCompatActivity() {

    private lateinit var imagePicker: ImagePicker
    private var imageUri: Uri? = null
    private var storagePostPictureRef: StorageReference? = null
    private  var myUrl=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_networking)
        storagePostPictureRef = FirebaseStorage.getInstance().reference.child("Post Picture")
        imagePicker = ImagePicker(this)
        redirectPostThoughts()
        selectOptionGallery()
        setImageOnStorage()
        hideThoughtLoader()
    }
    private fun hideThoughtLoader() {
        postThoughtLoader.visibility = View.GONE
    }

    private fun selectOptionGallery() {
        img_pick_btn.setOnClickListener {
            imagePicker.selectSource { imageResult ->
                imageCallBack(
                    imageResult
                )
            }
        }
    }

    private fun imageCallBack(imageResult: ImageResult<Uri>) {
        when (imageResult) {
            is ImageResult.Success -> {
                val uri = imageResult.value
                getLargeBitMap(uri)
            }
            is ImageResult.Failure -> {
                val errorString = imageResult.errorString
                Toast.makeText(this@Networking, errorString, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getLargeBitMap(uri: Uri) {
        imageUri = uri
        image_view.setImageURI(imageUri)
    }

    private fun redirectPostThoughts() {
        backToHome.setOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setImageOnStorage() {
        shareButton.setOnClickListener {
            when {
                imageUri == null -> Toast.makeText(this, "Please select image first.", Toast.LENGTH_LONG).show()
                TextUtils.isEmpty(textPost.text.toString()) -> Toast.makeText(this, "Please write caption", Toast.LENGTH_LONG).show()

                else -> {
                    displayThoughtLoader()
                    Toast.makeText(this, "Uploading...", Toast.LENGTH_LONG).show()
                    val fileRef = storagePostPictureRef!!.child(System.currentTimeMillis().toString() + ".jpg")

                    var uploadTask: StorageTask<*>
                    uploadTask = fileRef.putFile(imageUri!!)

                    uploadTask.continueWithTask(Continuation <UploadTask.TaskSnapshot, Task<Uri>> { task ->
                        if (!task.isSuccessful) {
                            task.exception?.let {
                                throw it
//                                Toast.makeText(this, "Something Went wrong...", Toast.LENGTH_LONG).show()
                            }
                        }
                        return@Continuation fileRef.downloadUrl
                    }).addOnCompleteListener(OnCompleteListener<Uri> { task ->
                        if (task.isSuccessful) {
                            val downloadUrl = task.result
                            myUrl = downloadUrl.toString()

                            val ref = FirebaseDatabase.getInstance().reference.child("Posts")
                            val postid = ref.push().key

                            val postMap = HashMap<String, Any>()

                            postMap["postid"] = postid!!
                            postMap["caption"] = textPost.text.toString()
                            postMap["publisher"] = FirebaseAuth.getInstance().currentUser!!.uid
                            postMap["postimage"] = myUrl

                            ref.child(postid).updateChildren(postMap)

                            val commentRef=FirebaseDatabase.getInstance().reference.child("Comment").child(postid)
                            val commentMap = HashMap<String, Any>()
                            commentMap["publisher"] = FirebaseAuth.getInstance().currentUser!!.uid
                            commentMap["comment"] =  textPost.text.toString()

                            commentRef.push().setValue(commentMap)

                            Toast.makeText(this, "Uploaded successfully", Toast.LENGTH_LONG).show()
                            val intent = Intent(this@Networking, ListActivity::class.java)
                            startActivity(intent)
                            finish()

                            hideThoughtLoader()
                        } else {
                            hideThoughtLoader()
                        }
                    })
                }
            }
        }
    }

    private fun displayThoughtLoader() {
        postThoughtLoader.visibility = View.VISIBLE
    }


}