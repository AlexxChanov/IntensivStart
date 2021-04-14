package ru.androidschool.intensiv.ui

import android.widget.ImageView
import com.squareup.picasso.Picasso
import ru.androidschool.intensiv.data.Genre
import ru.androidschool.intensiv.data.ProductionCompany
import java.lang.StringBuilder

fun setImage(source: String, target: ImageView) {
    Picasso.get()
        .load(source)
        .into(target)
}

fun genreStringBuilder(genres: List<Genre>): String {
    val sb = StringBuilder()

    for (n in genres.indices) {
        if (n != genres.size - 1) {
            sb.append("${genres[n].name}, ")
        } else sb.append(genres[n].name)
    }
    return sb.toString()
}

fun studiosStringBuilder(productionCompanies: List<ProductionCompany>): String {
    val sb = StringBuilder()

    for (n in productionCompanies.indices) {
        if (n != productionCompanies.size - 1) {
            sb.append("${productionCompanies[n].name}, ")
        } else sb.append(productionCompanies[n].name)
    }
    return sb.toString()
}
