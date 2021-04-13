package ru.androidschool.intensiv.data

import com.google.gson.annotations.SerializedName

data class Actor(
    val name : String,
    @SerializedName("profile_path")
    val profilePath: String
    ) {
    var profile : String = ""
        get() = "https://image.tmdb.org/t/p/w500$profilePath"
}
