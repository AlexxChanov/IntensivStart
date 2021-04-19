package ru.androidschool.intensiv.data

data class FeedFragmentDataContainer(
    val upcomingMovies : MutableList<Movie>,
    val popularMovies : MutableList<Movie>,
    val playingMovies : MutableList<Movie>
)