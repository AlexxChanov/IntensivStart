package ru.androidschool.intensiv.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Observable
import java.util.*

@Dao
interface LikedMovieDao {
    @Insert
    fun insertLikedMovie(movie: LikedMovie)

    @Delete
    fun delete(movie: LikedMovie)

    @Query("SELECT * FROM LikedMovie")
    fun getAllLiked(): Observable<List<LikedMovie>>

    @Query("SELECT * FROM LikedMovie WHERE movieId = :movieId")
    fun getLikedById(movieId: Int): Observable<LikedMovie>
}