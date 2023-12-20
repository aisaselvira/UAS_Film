package com.example.uas_film.roomDb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "film")
data class FilmEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "director")
    val director: String,
    @ColumnInfo(name = "writter")
    val writter: String,
    @ColumnInfo(name = "rating")
    val rating: String,
    @ColumnInfo(name = "sinopsis")
    val sinopsis: String,
    @ColumnInfo(name = "imageUrl")
    val imageUrl: String
) {
    // Add a no-argument constructor for Firebase deserialization
    constructor() : this(0, "", "","","","","")
}
