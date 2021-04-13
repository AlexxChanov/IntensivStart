package ru.androidschool.intensiv.data

data class TvShowResponse (
    val page : Int,
    val results: MutableList<TvShow>
    )