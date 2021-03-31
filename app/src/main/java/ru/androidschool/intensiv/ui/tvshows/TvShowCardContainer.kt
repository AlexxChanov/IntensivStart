package ru.androidschool.intensiv.ui.tvshows

import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_tv_show.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.Movie


class TvShowCardContainer (private val movie: Movie) : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.item_tv_show_name_tv.text = movie.title
        viewHolder.item_tv_show_rating_bar.rating = movie.rating

        Picasso.get()
            .load( R.drawable.placeholder)
            .into(viewHolder.item_tv_show_iv)
    }

    override fun getLayout() = R.layout.item_tv_show
}