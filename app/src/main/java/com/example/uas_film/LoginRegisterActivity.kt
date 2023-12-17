package com.example.uas_film

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.uas_film.databinding.ActivityLoginRegisterBinding
import com.google.android.material.tabs.TabLayoutMediator

class LoginRegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginRegisterBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // Check login status
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            val intent = Intent(this, Navigation::class.java)
            startActivity(intent)
            finish()
        } else {
            val adapter = LoginRegisAdapter(this)
            binding.viewPagerLogreg.adapter = adapter
            TabLayoutMediator(binding.tabLayoutLogreg, binding.viewPagerLogreg) { tab, position ->
                tab.text = when (position) {
                    0 -> "Login"
                    1 -> "Register"
                    else -> "Invalid"
                }
            }.attach()
        }
    }
}
