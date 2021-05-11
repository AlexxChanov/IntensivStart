package ru.androidschool.intensiv.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import ru.androidschool.intensiv.BuildConfig

@Parcelize
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
) : Parcelable {

    @IgnoredOnParcel
    val poster: String
        get() = "${BuildConfig.IMAGE_URL}$posterPath"

    @IgnoredOnParcel
    val rating: Float
        get() = voteAverage.div(2).toFloat()
}
