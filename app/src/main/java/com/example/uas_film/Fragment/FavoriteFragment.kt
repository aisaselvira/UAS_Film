package com.example.uas_film.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uas_film.Adapter.FavoriteAdapter
import com.example.uas_film.Model.Favorite
import com.example.uas_film.R
import com.google.firebase.database.*

class FavoriteFragment : Fragment() {

    private lateinit var favoriteAdapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Change the layout resource ID to the correct one
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.rv_favorite)
        favoriteAdapter = FavoriteAdapter(ArrayList())

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = favoriteAdapter

        val favoriteOrderReference = FirebaseDatabase.getInstance().getReference("Favorite")
        favoriteOrderReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val favoriteList = ArrayList<Favorite>()
                for (snapshot in dataSnapshot.children) {
                    val favoriteData = snapshot.getValue(Favorite::class.java)
                    if (favoriteData != null) {
                        favoriteList.add(favoriteData)
                    }
                }
                favoriteAdapter.addFavorites(favoriteList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "Error getting data", databaseError.toException())
            }
        })
    }

    companion object {
        const val TAG = "FavoriteFragment"
    }
}
