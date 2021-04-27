package ru.androidschool.intensiv.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LikedMovie (
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val movieId: Int,
    val title: String,
    val voteAverage: Double,
    val posterPath: String,
    val adult: Boolean,
    val overview: String,
    val releaseDate: String,
    val genreIds: ArrayList<Int>,
    val originalTitle: String,
    val originalLanguage: String,
    val backdropPath: String,
    val popularity: Double,
    val voteCount: Int,
    val video: Boolean
)