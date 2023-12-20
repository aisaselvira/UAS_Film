package com.example.uas_film

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {

    private lateinit var orderData: FilmAdminData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Get data from the intent
        val title = intent.getStringExtra("title")
        val director = intent.getStringExtra("director")
        val writter = intent.getStringExtra("writter")
        val rating = intent.getStringExtra("rating")
        val sinopsis = intent.getStringExtra("sinopsis")
        val imageUrl = intent.getStringExtra("imageUrl")

        // Set the data to the corresponding views in your layout
        val titleTextView: TextView = findViewById(R.id.title_film_detail)
        val directorTextView: TextView = findViewById(R.id.director_detail) // Corrected the ID
        val writterTextView: TextView = findViewById(R.id.writter_detail)
        val ratingTextView: TextView = findViewById(R.id.rating_detail)
        val sinopsisTextView: TextView = findViewById(R.id.sinopsis_detail)
        val imageImageView: ImageView = findViewById(R.id.image_film_detail)

        titleTextView.text = title
        directorTextView.text = director
        writterTextView.text = writter
        ratingTextView.text = rating
        sinopsisTextView.text = sinopsis

        // Use Glide or Picasso to load the image from the URL into the ImageView
        Glide.with(this)
            .load(imageUrl)
            .into(imageImageView)


    }
}
