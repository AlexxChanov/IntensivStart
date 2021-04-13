package ru.androidschool.intensiv.ui.tvshows

import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_tv_show.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.Movie
import ru.androidschool.intensiv.data.TvShow

class TvShowCardContainer(private val show: TvShow) : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.item_tv_show_name_tv.text = show.name
        viewHolder.item_tv_show_rating_bar.rating = show.rating

        Picasso.get()
            .load(show.poster)
            .into(viewHolder.item_tv_show_iv)
    }

    override fun getLayout() = R.layout.item_tv_show
}
