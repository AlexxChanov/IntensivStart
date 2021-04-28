package ru.androidschool.intensiv.ui

import android.widget.ImageView
import com.squareup.picasso.Picasso
import java.lang.StringBuilder

fun setImage(source: String, target: ImageView) {
    Picasso.get()
        .load(source)
        .into(target)
}

fun namesStringBuilder(names: List<String>): String {
    val sb = StringBuilder()

    for (n in names.indices) {
        if (n != names.size - 1) {
            sb.append("${names[n]}, ")
        } else sb.append(names[n])
    }
    return sb.toString()
}
