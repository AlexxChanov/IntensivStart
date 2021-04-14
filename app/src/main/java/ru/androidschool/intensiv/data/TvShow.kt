package ru.androidschool.intensiv.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TvShow(
    @SerializedName("first_air_date")
    val firstAirDate: String,
    @SerializedName("genre_ids")
    val genreIds: MutableList<Int>,
    val id: Int,
    val name: String,
    val overview: String,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("vote_average")
    val voteAverage: Float
) : Parcelable {
    var poster: String = ""
        get() = "https://image.tmdb.org/t/p/w500$posterPath"

    val rating: Float
        get() = voteAverage.div(2).toFloat()
}
