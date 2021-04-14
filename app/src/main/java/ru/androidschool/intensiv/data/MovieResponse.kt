package ru.androidschool.intensiv.data

data class MovieResponse(
    val page: Int,
    val results: MutableList<Movie>
)
