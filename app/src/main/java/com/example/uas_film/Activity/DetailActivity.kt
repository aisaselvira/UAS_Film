package com.example.uas_film.Activity

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.uas_film.Model.Favorite
import com.example.uas_film.R
import com.example.uas_film.databinding.ActivityDetailBinding
import com.google.firebase.database.FirebaseDatabase

class DetailActivity : AppCompatActivity() {

    private lateinit var favoriteData: Favorite
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

        val titleTextView: TextView = findViewById(R.id.title_film_detail)
        val directorTextView: TextView = findViewById(R.id.director_detail)
        val writterTextView: TextView = findViewById(R.id.writter_detail)
        val ratingTextView: TextView = findViewById(R.id.rating_detail)
        val sinopsisTextView: TextView = findViewById(R.id.sinopsis_detail)
        val imageImageView: ImageView = findViewById(R.id.image_film_detail)

        titleTextView.text = title
        directorTextView.text = director
        writterTextView.text = writter
        ratingTextView.text = rating
        sinopsisTextView.text = sinopsis

        Glide.with(this)
            .load(imageUrl)
            .into(imageImageView)

        val favButton: ImageButton = findViewById(R.id.btn_fav)
        favButton.setOnClickListener {

            favoriteData = Favorite(
                title = title.orEmpty(),
                director = director.orEmpty(),
                writter = writter.orEmpty(),
                rating = rating.orEmpty(),
                sinopsis = sinopsis.orEmpty(),
                imageUrl = imageUrl.orEmpty(),  // Ini sudah diinisialisasi di awal
                favoriteId = ""
            )

            val favoriteReference = FirebaseDatabase.getInstance().getReference("Favorite")
            val favoriteId = favoriteReference.push().key
            favoriteData.favoriteId = favoriteId.orEmpty()
            favoriteReference.child(favoriteId.orEmpty()).setValue(favoriteData)

            setResult(RESULT_OK)
            finish()
        }

        // Set the data to the corresponding views in your layout using binding
        with(binding) {
            // Set OnClickListener for buttonBack
            buttonBack.setOnClickListener{
                onBackPressed()
            }
        }
    }
}
