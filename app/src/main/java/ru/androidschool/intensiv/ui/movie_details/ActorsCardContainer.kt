package ru.androidschool.intensiv.ui.movie_details

import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_actor.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.Actor
import ru.androidschool.intensiv.ui.setImage

class ActorsCardContainer(
    private val actor: Actor

) : Item() {

    override fun getLayout() = R.layout.item_actor

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.item_actor_name.text = actor.name
        setImage(actor.profile, viewHolder.item_actor_image)
    }
}
