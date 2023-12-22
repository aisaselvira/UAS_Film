package com.example.uas_film.Model

data class Favorite(
    val title: String,
    val director: String,
    val writter: String,
    val rating: String,
    val sinopsis: String,
    var favoriteId: String,
    var imageUrl: String
) {
    // Add a no-argument constructor for Firebase deserialization
    constructor(

    ) : this("", "", "",  "", "", "", "")
}