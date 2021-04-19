package ru.androidschool.intensiv.data

import android.os.Build
import com.google.gson.annotations.SerializedName
import ru.androidschool.intensiv.BuildConfig

data class Actor(
    val name: String,
    @SerializedName("profile_path")
    val profilePath: String
) {
    var profile: String = ""
        get() = "${BuildConfig.IMAGE_URL}$profilePath"
}
