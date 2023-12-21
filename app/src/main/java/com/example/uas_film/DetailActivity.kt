package com.example.uas_film

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.example.uas_film.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get data from the intent
        val title = intent.getStringExtra("title")
        val director = intent.getStringExtra("director")
        val writter = intent.getStringExtra("writter")
        val rating = intent.getStringExtra("rating")
        val sinopsis = intent.getStringExtra("sinopsis")
        val imageUrl = intent.getStringExtra("imageUrl")

        // Set the data to the corresponding views in your layout using binding
        with(binding) {
            titleFilmDetail.text = title
            directorDetail.text = director
            writterDetail.text = writter
            ratingDetail.text = rating
            sinopsisDetail.text = sinopsis

            // Use Glide or Picasso to load the image from the URL into the ImageView
            Glide.with(this@DetailActivity)
                .load(imageUrl)
                .into(imageFilmDetail)

            // Set OnClickListener for buttonBack
            buttonBack.setOnClickListener{
                onBackPressed()
            }
        }
    }
}
