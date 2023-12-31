package com.example.uas_film.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.uas_film.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            val btnStart: Button = btnStart

            btnStart.setOnClickListener {

                val intent = Intent(this@MainActivity, LoginRegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
