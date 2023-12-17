package com.example.uas_film

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class FilmUserAdapter(private val filmUserList: ArrayList<FilmUserData>) : RecyclerView.Adapter<FilmUserAdapter.FilmUserViewHolder>() {

    class FilmUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleUser: TextView = itemView.findViewById(R.id.title_film_user)
        val imageUser: ImageView = itemView.findViewById(R.id.image_film_user)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmUserViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_film, parent, false)
        return FilmUserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FilmUserViewHolder, position: Int) {
        val currentItem = filmUserList[position]
        holder.titleUser.setText(currentItem.title)

        holder.titleUser.text = currentItem.title
        // Use Glide or Picasso to load the image from the URL into the ImageView
        Glide.with(holder.itemView.context)
            .load(currentItem.imageUrl)
            .into(holder.imageUser)
    }

    override fun getItemCount() = filmUserList.size
}
