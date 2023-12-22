package com.example.uas_film.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uas_film.Adapter.FilmAdapter
import com.example.uas_film.Model.FilmAdminData
import com.example.uas_film.R
import com.example.uas_film.databinding.ActivityHomeAdminBinding
import com.google.firebase.database.*

class HomeAdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeAdminBinding
    private lateinit var itemAdapter: FilmAdapter
    private lateinit var itemList: ArrayList<FilmAdminData>
    private lateinit var recyclerViewItem: RecyclerView
    private lateinit var database: DatabaseReference
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerViewItem = binding.rvFilm
        recyclerViewItem.setHasFixedSize(true)
        recyclerViewItem.layoutManager = LinearLayoutManager(this)

        itemList = arrayListOf()
        itemAdapter = FilmAdapter(itemList)
        recyclerViewItem.adapter = itemAdapter

        binding.btnPlusAdmin.setOnClickListener {
            startActivity(Intent(this, FilmAdminAdd::class.java))
        }

        database = FirebaseDatabase.getInstance().getReference("Film")
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                itemList.clear()

                for (dataSnapshot in snapshot.children) {
                    val item = dataSnapshot.getValue(FilmAdminData::class.java)
                    if (item != null) {
                        itemList.add(item)
                    }
                }
                itemAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error if needed
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_logout, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {
                logoutAdmin()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }



    private fun logoutAdmin() {
        // Clear user session, update SharedPreferences, etc.
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", false)
        editor.apply()

        // Show a toast message
        Toast.makeText(this@HomeAdminActivity, "Logout successful", Toast.LENGTH_SHORT).show()

        // Redirect to the login screen or perform other actions as needed
        // For example, you can use the following code:
        val intent = Intent(this, LoginRegisterActivity::class.java)
        startActivity(intent)
        finish()
    }

}
