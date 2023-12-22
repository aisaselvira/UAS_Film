package com.example.uas_film.Adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.uas_film.Model.FilmAdminData
import com.example.uas_film.R
import com.example.uas_film.Activity.EditAdminActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class FilmAdapter(private val filmAdminList: ArrayList<FilmAdminData>) : RecyclerView.Adapter<FilmAdapter.FilmViewHolder>() {

    override fun getItemCount(): Int {
        return filmAdminList.size
    }

    class FilmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title_film)
        val director: TextView = itemView.findViewById(R.id.director_film)
        val writer: TextView = itemView.findViewById(R.id.writer_film)
        val rating: TextView = itemView.findViewById(R.id.rating_film)
        val image: ImageView = itemView.findViewById(R.id.image_film)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_film, parent, false)
        return FilmViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        val currentItem = filmAdminList[position]
        holder.title.setText(currentItem.title)
        holder.director.setText(currentItem.director)
        holder.writer.setText(currentItem.writter)
        holder.rating.setText(currentItem.rating)

        // Use Glide or Picasso to load the image from the URL into the ImageView
        Glide.with(holder.itemView.context)
            .load(currentItem.imageUrl)
            .skipMemoryCache(true) // Skip caching in memory
            .diskCacheStrategy(DiskCacheStrategy.NONE) // Skip caching on disk
            .into(holder.image)

        holder.itemView.findViewById<ImageButton>(R.id.btn_edit).setOnClickListener{
            val intent = Intent(holder.itemView.context, EditAdminActivity::class.java)
            val currentItem = filmAdminList[position]
            intent.putExtra("title", currentItem.title)
            intent.putExtra("director", currentItem.director)
            intent.putExtra("writter", currentItem.writter)
            intent.putExtra("rating", currentItem.rating)
            intent.putExtra("sinopsis", currentItem.sinopsis)
            intent.putExtra("imgId", currentItem.imageUrl)
            holder.itemView.context.startActivity(intent)
        }

        holder.itemView.findViewById<ImageButton>(R.id.btn_hapus).setOnClickListener{
            val itemToDelete = Uri.parse(filmAdminList[position].imageUrl.toString()).lastPathSegment?.removePrefix("images/")

            // Remove the item from the list
            filmAdminList.removeAt(position)

            // Notify the adapter of the data change
            notifyDataSetChanged()

            // Delete the corresponding data from the Realtime Database
            deleteItemFromDatabase(itemToDelete.toString())
        }

    }

    private fun deleteItemFromDatabase(imgId: String) {
        // Reference to the Firebase Storage
        val storageReference = FirebaseStorage.getInstance().getReference("images").child(imgId)

        // Delete the image from Firebase Storage
        storageReference.delete().addOnSuccessListener {
            // Image deleted successfully, now delete the corresponding data from the Realtime Database
            val database = FirebaseDatabase.getInstance().getReference("Film")
            database.child(imgId).removeValue()
                .addOnCompleteListener {
                    // Handle success if needed
                }
                .addOnFailureListener {
                    // Handle failure if needed
                }
        }.addOnFailureListener {
            // Handle failure if the image deletion fails
        }
    }

}
