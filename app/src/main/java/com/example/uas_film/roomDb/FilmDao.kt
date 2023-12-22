package com.example.uas_film.roomDb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FilmDao {

    @Query("SELECT * FROM film")
    fun getAllFilm(): LiveData<List<FilmEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFilm(film: List<FilmEntity>)

    @Delete
    suspend fun deleteFilm(film: FilmEntity)

    @Query("DELETE FROM Film")
    suspend fun deleteAllFilm()
}