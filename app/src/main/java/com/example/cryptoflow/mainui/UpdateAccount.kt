package com.example.cryptoflow.mainui

import android.app.Activity
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
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_networking.*
import kotlinx.android.synthetic.main.activity_update_account.*

class UpdateAccount : AppCompatActivity() {
    private  var imageUri: Uri?=null
    private  var myProfUrl=""
    private lateinit var database : DatabaseReference
    private lateinit var cropActivityResultLauncher: ActivityResultLauncher<Any?>
    private  var storageProfileRef: StorageReference?=null
    private lateinit var firebaseUser: FirebaseUser

    private val cropActivityResultContract = object : ActivityResultContract<Any?, Uri?>() {
        override fun createIntent(context: Context, input: Any?): Intent {
            return CropImage.activity()
                .setAspectRatio(1, 1)
                .getIntent(this@UpdateAccount)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            return CropImage.getActivityResult(intent)?.uri
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_account)
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        storageProfileRef = FirebaseStorage.getInstance().reference.child("Profile Pictures")


        updateData()
        hideUpdateLoader()
        cropActivityResultLauncher = registerForActivityResult(cropActivityResultContract) {
            it?.let { uri ->
                imageUri = uri
                updateProfImg.setImageURI(imageUri)
            }
        }

        changeImg.setOnClickListener {
            cropActivityResultLauncher.launch(null)
        }
    }

    private fun updateData() {
        updatebutton.setOnClickListener {
            when {
                imageUri == null -> Toast.makeText(this, "Please select image first.", Toast.LENGTH_LONG).show()
                TextUtils.isEmpty(prefUsername.text.toString()) -> Toast.makeText(this, "Please write caption", Toast.LENGTH_LONG).show()

                else -> {
                    displayUpdateLoader()
                    Toast.makeText(this, "Updating...", Toast.LENGTH_LONG).show()
                    val fileRef = storageProfileRef!!.child(firebaseUser.uid + ".jpg")

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
                            myProfUrl = downloadUrl.toString()

                            val ref = FirebaseDatabase.getInstance().reference.child("Users")

                            val userMap = HashMap<String, Any>()


                            userMap["username"] = prefUsername.text.toString()
                            userMap["image"] = myProfUrl

                            ref.child(firebaseUser.uid).updateChildren(userMap)


                            Toast.makeText(this, "Uploaded successfully", Toast.LENGTH_LONG).show()
                            val intent = Intent(this@UpdateAccount, Profile::class.java)
                            startActivity(intent)
                            finish()
                            hideUpdateLoader()
                        } else {
                            hideUpdateLoader()
                        }
                    })
                }
            }
        }
    }

    private fun hideUpdateLoader() {
        updateLoader.visibility = View.GONE
    }

    private fun displayUpdateLoader() {
        updateLoader.visibility = View.VISIBLE
    }
}