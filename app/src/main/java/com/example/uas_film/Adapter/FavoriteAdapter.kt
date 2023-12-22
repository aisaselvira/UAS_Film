package com.example.uas_film.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.uas_film.Model.Favorite
import com.example.uas_film.R

class FavoriteAdapter(private val favoriteList: MutableList<Favorite>) :
    RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.title_favorite)
        val directorTextView: TextView = itemView.findViewById(R.id.director_favorite)
        val writterTextView: TextView = itemView.findViewById(R.id.writer_favorite)
        val ratingTextView: TextView = itemView.findViewById(R.id.rating_favorite)
        val sinopsisTextView: TextView = itemView.findViewById(R.id.sinopsis_favorite)
        val imageView: ImageView = itemView.findViewById(R.id.image_favorite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favorite, parent, false)
        return FavoriteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val currentFavorite = favoriteList[position] // Fix variable name

        // Use appropriate property names based on your Favorite model
        holder.titleTextView.text = currentFavorite.title
        holder.directorTextView.text = currentFavorite.director
        holder.writterTextView.text = currentFavorite.writter
        holder.ratingTextView.text = currentFavorite.rating
        holder.sinopsisTextView.text = currentFavorite.rating

        Glide.with(holder.imageView.context)
            .load(currentFavorite.imageUrl)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return favoriteList.size
    }

    fun addFavorites(favorites: List<Favorite>) {
        favoriteList.clear()
        favoriteList.addAll(favorites)
        notifyDataSetChanged()
    }
}
