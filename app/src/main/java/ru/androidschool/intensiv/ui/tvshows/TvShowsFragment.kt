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
import kotlinx.android.synthetic.main.tv_shows_fragment.*
import retrofit2.Call
import retrofit2.Response
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

    private fun getPopularTvShows(){
        MovieApiClient.apiClient.getPopularTvShows()
            .subscribeOn(Schedulers.io())
            .map { popularTvShows = it.results }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { error -> Timber.d(error) }
            .doOnComplete { init() }
            .subscribe()
    }

}
