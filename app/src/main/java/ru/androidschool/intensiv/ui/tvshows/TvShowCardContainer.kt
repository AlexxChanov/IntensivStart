package ru.androidschool.intensiv.ui.tvshows

import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_tv_show.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.TvShow
import ru.androidschool.intensiv.ui.setImage

class TvShowCardContainer(private val show: TvShow) : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.item_tv_show_name_tv.text = show.name
        viewHolder.item_tv_show_rating_bar.rating = show.rating

        setImage(show.poster, viewHolder.item_tv_show_iv)
    }

    override fun getLayout() = R.layout.item_tv_show
}
