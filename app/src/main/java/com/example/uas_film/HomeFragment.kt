package com.example.uas_film

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uas_film.databinding.FragmentHomeBinding
import com.google.firebase.database.*

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var itemAdapter: FilmUserAdapter
    private lateinit var itemList: ArrayList<FilmUserData>
    private lateinit var recyclerViewItem: RecyclerView

    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewItem = binding.rvFilm
        recyclerViewItem.setHasFixedSize(true)

        // Use GridLayoutManager with a span count of 2 for two columns
        recyclerViewItem.layoutManager = GridLayoutManager(requireContext(), 2)

        itemList = arrayListOf()
        itemAdapter = FilmUserAdapter(itemList)
        recyclerViewItem.adapter = itemAdapter

        database = FirebaseDatabase.getInstance().getReference("Film")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                itemList.clear()

                for (dataSnapshot in snapshot.children) {
                    val item = dataSnapshot.getValue(FilmUserData::class.java)
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
}
