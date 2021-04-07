package ru.androidschool.intensiv.data

import com.google.gson.annotations.SerializedName

data class Movie(
    var title: String?,
    @SerializedName("vote_average")
    var voteAverage: Double,
    @SerializedName("poster_path")
    val posterPath: String,
    val adult: Boolean,
    val overview: String,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("genre_ids")
    val genreIds: List<Int>,
    val id: Int,
    @SerializedName("original_title")
    val originalTitle: String,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("backdrop_path")
    val backdropPath: String,
    val popularity: Double,
    @SerializedName("vote_count")
    val voteCount: Int,
    val video: Boolean
)

/**
 *      "poster_path": "/e1mjopzAS2KNsvpbpahQ1a6SkSn.jpg",
"adult": false,
"overview": "From DC Comics comes the Suicide Squad, an antihero team of incarcerated supervillains who act as deniable assets for the United States government, undertaking high-risk black ops missions in exchange for commuted prison sentences.",
"release_date": "2016-08-03",
"genre_ids": [
14,
28,
80
],
"id": 297761,
"original_title": "Suicide Squad",
"original_language": "en",
"title": "Suicide Squad",
"backdrop_path": "/ndlQ2Cuc3cjTL7lTynw6I4boP4S.jpg",
"popularity": 48.261451,
"vote_count": 1466,
"video": false,
"vote_average": 5.91
 *
 * */
