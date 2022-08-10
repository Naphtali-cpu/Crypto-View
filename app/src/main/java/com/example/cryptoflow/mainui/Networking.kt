package com.example.cryptoflow.mainui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
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
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.android.synthetic.main.activity_networking.*

class Networking : AppCompatActivity() {

    private var imageUri: Uri? = null
    private var storagePostPictureRef: StorageReference? = null
    private  var myUrl=""

    private val cropActivityResultContract = object : ActivityResultContract<Any?, Uri?>() {
        override fun createIntent(context: Context, input: Any?): Intent {
            return CropImage.activity()
                .setAspectRatio(16, 10)
                .getIntent(this@Networking)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            return CropImage.getActivityResult(intent)?.uri
        }

    }

    private lateinit var cropActivityResultLauncher: ActivityResultLauncher<Any?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_networking)
        storagePostPictureRef = FirebaseStorage.getInstance().reference.child("Post Picture")
        redirectPostThoughts()
        setImageOnStorage()
        hideThoughtLoader()
        cropActivityResultLauncher = registerForActivityResult(cropActivityResultContract) {
            it?.let { uri ->
                imageUri = uri
                image_view.setImageURI(imageUri)
            }
        }

        img_pick_btn.setOnClickListener {
            cropActivityResultLauncher.launch(null)
        }
    }
    private fun hideThoughtLoader() {
        postThoughtLoader.visibility = View.GONE
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