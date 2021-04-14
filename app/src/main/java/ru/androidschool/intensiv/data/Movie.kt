package ru.androidschool.intensiv.data

import android.os.Parcel
import android.os.Parcelable

class Movie(
    var title: String? = "",
    var voteAverage: Double = 0.0
) : Parcelable {
    val rating: Float
        get() = voteAverage.div(2).toFloat()

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readDouble()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeDouble(voteAverage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Movie> {
        override fun createFromParcel(parcel: Parcel): Movie {
            return Movie(parcel)
        }

        override fun newArray(size: Int): Array<Movie?> {
            return arrayOfNulls(size)
        }
    }
}