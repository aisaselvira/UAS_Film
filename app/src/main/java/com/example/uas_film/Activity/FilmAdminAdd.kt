package com.example.uas_film.Activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.uas_film.Model.FilmAdminData
import com.example.uas_film.databinding.ActivityAddFilmAdminBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.util.UUID

class FilmAdminAdd : AppCompatActivity() {
    private lateinit var binding: ActivityAddFilmAdminBinding

    private lateinit var database: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var imageUri: Uri

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                imageUri = uri
                binding.imgViewAdd.setImageURI(uri)
                // Optionally, you can call uploadData(imageUri) here if needed
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFilmAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAdd.setOnClickListener {
            uploadData(imageUri)
        }

        binding.btnChooseImage.setOnClickListener {
            getContent.launch("image/*")
        }

        // Set OnClickListener for buttonBack
        binding.buttonBack.setOnClickListener{
            onBackPressed()
        }

    }

    private fun uploadData(imageUri: Uri? = null) {
        val title: String = binding.txtName.text.toString()
        val director: String = binding.txtDirector.text.toString()
        val writter: String = binding.txtWritter.text.toString()
        val rating: String = binding.txtRating.text.toString()
        val sinopsis: String = binding.txtSinopsis.text.toString()

        val imageId = UUID.randomUUID().toString()

        if (title.isNotEmpty() && director.isNotEmpty() && writter.isNotEmpty() && rating.isNotEmpty() && sinopsis.isNotEmpty() && imageUri != null) {
            // Generate a unique ID for the image

            // Upload image to Firebase Storage with the generated ID
            storageReference = FirebaseStorage.getInstance().reference.child("images/$imageId")
            val uploadTask: UploadTask = storageReference.putFile(imageUri)

            uploadTask.addOnSuccessListener {
                // Image uploaded successfully, now get the download URL
                storageReference.downloadUrl.addOnSuccessListener { imageUrl ->
                    val item = FilmAdminData(title, director, writter, rating, sinopsis, imageUrl.toString())
                    database = FirebaseDatabase.getInstance().getReference("Film")
                    database.child(imageId).setValue(item)
                        .addOnCompleteListener {
                            binding.txtName.text!!.clear()
                            binding.txtDirector.text!!.clear()
                            binding.txtWritter.text!!.clear()
                            binding.txtRating.text!!.clear()
                            binding.txtSinopsis.text!!.clear()
                            Toast.makeText(this, "Data Uploaded Successfully", Toast.LENGTH_SHORT).show()

                            // Navigate back to HomeAdminActivity
                            val intent = Intent(this, HomeAdminActivity::class.java)
                            startActivity(intent)
                            finish() // Finish the current activity to prevent going back to it with the back button
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Adding Data Failed!", Toast.LENGTH_SHORT).show()
                        }
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Image Upload Failed!", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Please fill in all fields and select an image", Toast.LENGTH_SHORT).show()
        }
    }
}
