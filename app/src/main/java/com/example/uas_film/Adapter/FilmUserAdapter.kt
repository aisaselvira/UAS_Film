package com.example.uas_film.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.uas_film.R
import com.example.uas_film.roomDb.FilmEntity

class FilmUserAdapter(private var filmUserList: List<FilmEntity>) : RecyclerView.Adapter<FilmUserAdapter.FilmUserViewHolder>() {

    private var listener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    // Fungsi untuk mendapatkan item di posisi tertentu
    fun getItemAtPosition(position: Int): FilmEntity {
        return filmUserList[position]
    }

    inner class FilmUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val titleUser: TextView = itemView.findViewById(R.id.title_film_user)
        val imageUser: ImageView = itemView.findViewById(R.id.image_film_user)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener?.onItemClick(adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmUserViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_film, parent, false)
        return FilmUserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FilmUserViewHolder, position: Int) {
        val currentItem = filmUserList[position]
        holder.titleUser.text = currentItem.title

        // Use Glide or Picasso to load the image from the URL into the ImageView
        Glide.with(holder.itemView.context)
            .load(currentItem.imageUrl)
            .into(holder.imageUser)
    }

    override fun getItemCount() = filmUserList.size

    fun updateData(newList: List<FilmEntity>) {
        filmUserList = newList
        notifyDataSetChanged()
    }
}