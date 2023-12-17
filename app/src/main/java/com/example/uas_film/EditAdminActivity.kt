package com.example.uas_film

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.uas_film.databinding.ActivityEditAdminBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.util.UUID

class EditAdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditAdminBinding
    private lateinit var database: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var imageUri: Uri

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                imageUri = uri
                binding.imgViewEdit.setImageURI(uri)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnChooseImage.setOnClickListener {
            getContent.launch("image/*")
        }

        val title = binding.txtTitleEdit
        val director = binding.txtDirectorEdit
        val writter = binding.txtWritterEdit
        val rating = binding.txtRatingEdit
        val sinopsis = binding.txtSinopsisEdit

        val originalImageUrl = intent.getStringExtra("imgId")
        Glide.with(this)
            .load(originalImageUrl)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(binding.imgViewEdit)

        title.setText(intent.getStringExtra("title"))
        director.setText(intent.getStringExtra("director"))
        writter.setText(intent.getStringExtra("writter"))
        rating.setText(intent.getStringExtra("rating"))
        sinopsis.setText(intent.getStringExtra("sinopsis"))

        binding.btnUpdate.setOnClickListener {
            uploadData(imageUri)
        }
    }

    private fun uploadData(imageUri: Uri? = null) {
        val updatedTitle = binding.txtTitleEdit.text.toString()
        val updatedDirector = binding.txtDirectorEdit.text.toString()
        val updatedWritter = binding.txtWritterEdit.text.toString()
        val updatedRating = binding.txtRatingEdit.text.toString()
        val updatedSinopsis = binding.txtSinopsisEdit.text.toString()

        database = FirebaseDatabase.getInstance().getReference("Film")

        if (imageUri != null) {
            // Generate a unique ID for the image
            val imageId = UUID.randomUUID().toString()

            // Upload image to Firebase Storage with the generated ID
            storageReference = FirebaseStorage.getInstance().reference.child("images/$imageId")
            val uploadTask: UploadTask = storageReference.putFile(imageUri)

            uploadTask.addOnSuccessListener {
                // Image uploaded successfully, now get the download URL
                storageReference.downloadUrl.addOnSuccessListener { imageUrl ->
                    val item = FilmAdminData(
                        updatedTitle,
                        updatedDirector,
                        updatedWritter,
                        updatedRating,
                        updatedSinopsis,
                        imageUrl.toString()
                    )
                    database.child(imageId).setValue(item)
                        .addOnCompleteListener {
                            // Handle completion, e.g., show a success message
                            Toast.makeText(this, "Data Uploaded Successfully", Toast.LENGTH_SHORT).show()

                            // Start HomeAdminActivity after successful update
                            val intent = Intent(this, HomeAdminActivity::class.java)
                            startActivity(intent)

                            // Finish current activity
                            finish()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Adding Data Failed!", Toast.LENGTH_SHORT).show()
                        }
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Image Upload Failed!", Toast.LENGTH_SHORT).show()
            }
        } else {
            // If no new image is selected, update the data without uploading a new image
            val imageId = intent.getStringExtra("imgId")!!

            val updatedList = mapOf(
                "title" to updatedTitle,
                "director" to updatedDirector,
                "writter" to updatedWritter,
                "rating" to updatedRating,
                "sinopsis" to updatedSinopsis
            )

            // Update the data with the new title
            database.child(imageId).updateChildren(updatedList)
                .addOnCompleteListener {
                    // Handle completion, e.g., show a success message
                    Toast.makeText(this, "Data Updated Successfully", Toast.LENGTH_SHORT).show()

                    // Start HomeAdminActivity after successful update
                    val intent = Intent(this, HomeAdminActivity::class.java)
                    startActivity(intent)

                    // Finish current activity
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Updating Data Failed!", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
