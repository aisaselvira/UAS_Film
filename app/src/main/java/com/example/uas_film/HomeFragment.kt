package com.example.uas_film

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uas_film.databinding.FragmentHomeBinding
import com.example.uas_film.roomDb.FilmDao
import com.example.uas_film.roomDb.FilmEntity
import com.example.uas_film.roomDb.FilmRoomDatabase
import com.google.firebase.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var itemAdapter: FilmUserAdapter
    private lateinit var recyclerViewItem: RecyclerView
    private lateinit var itemList: ArrayList<FilmAdminData>

    private lateinit var database: DatabaseReference
    private lateinit var filmDao: FilmDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewItem = binding.rvFilm
        recyclerViewItem.setHasFixedSize(true)
        recyclerViewItem.layoutManager = GridLayoutManager(requireContext(), 2)

        itemAdapter = FilmUserAdapter(emptyList()) // Initial data is empty
        recyclerViewItem.adapter = itemAdapter

        // Set item click listener
        itemAdapter.setOnItemClickListener(object : FilmUserAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                // Handle item click here
                val clickedItem: FilmEntity = itemAdapter.getItemAtPosition(position)

                // Create an intent to start DetailActivity
                val intent = Intent(requireContext(), DetailActivity::class.java)

                // Put the necessary data into the intent
                intent.putExtra("title", clickedItem.title)
                intent.putExtra("director", clickedItem.director)
                intent.putExtra("writter", clickedItem.writter)
                intent.putExtra("rating", clickedItem.rating)
                intent.putExtra("sinopsis", clickedItem.sinopsis)
                intent.putExtra("imageUrl", clickedItem.imageUrl)

                // Start DetailActivity with the intent
                startActivity(intent)
            }
        })


        // Initialize Room database
        filmDao = FilmRoomDatabase.getDatabase(requireContext()).filmDao()

        // Initialize Firebase reference
        database = FirebaseDatabase.getInstance().getReference("Film")

        // Fetch data from Firebase and update itemList
        fetchFilmFromFirebase()
    }

    private fun fetchFilmFromFirebase() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val filmList = mutableListOf<FilmEntity>()

                for (dataSnapshot in snapshot.children) {
                    val filmEntity = dataSnapshot.getValue(FilmEntity::class.java)
                    filmEntity?.let { filmList.add(it) }
                }

                // Update Room database with the new data from Firebase
                GlobalScope.launch(Dispatchers.IO) {
                    filmDao.deleteAllFilm()
                    filmDao.insertFilm(filmList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error if needed
            }
        })

        // Observe changes in the LiveData from Room and update the adapter
        filmDao.getAllFilm().observe(viewLifecycleOwner, Observer { films ->
            itemAdapter.updateData(films)
        })
    }
}
