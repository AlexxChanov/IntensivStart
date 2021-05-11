package ru.androidschool.intensiv.ui.tvshows

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.tv_shows_fragment.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.*
import ru.androidschool.intensiv.network.MovieApiClient
import timber.log.Timber

class TvShowsFragment : Fragment() {

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    lateinit var popularTvShows: MutableList<TvShow>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.tv_shows_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getPopularTvShows()
    }

    private fun init() {
        tv_shows_recyclerview.adapter = adapter.apply { addAll(listOf()) }

        val moviesList = popularTvShows.map {
            TvShowCardContainer(it)
        }
        tv_shows_recyclerview.adapter = adapter.apply { addAll(moviesList) }
    }

    private fun getPopularTvShows() {
        MovieApiClient.apiClient.getPopularTvShows()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { inProgress(true) }
            .doFinally { inProgress(false) }
            .subscribe({ result -> setData(result.results) }, { error -> Timber.e(error) })
    }

    private fun setData(tvShows: MutableList<TvShow>) {
        popularTvShows = tvShows
        init()
    }

    private fun inProgress(inProgress: Boolean) {
        if (inProgress) {
            tv_shows_loader.visibility = View.VISIBLE
            tv_shows_recyclerview.visibility = View.GONE
        } else {
            tv_shows_loader.visibility = View.GONE
            tv_shows_recyclerview.visibility = View.VISIBLE
        }
    }
}
