package ru.androidschool.intensiv.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.feed_header.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.search_toolbar.*
import kotlinx.android.synthetic.main.tv_shows_fragment.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.Movie
import ru.androidschool.intensiv.network.MovieApiClient
import ru.androidschool.intensiv.ui.feed.FeedFragment.Companion.KEY_SEARCH
import ru.androidschool.intensiv.ui.tvshows.TvShowCardContainer
import timber.log.Timber
import java.util.concurrent.TimeUnit


class SearchFragment : Fragment(R.layout.fragment_search) {

    private var searchingText: String? = null
    private var searchingMovieslist = mutableListOf<Movie>()
    lateinit var filterSubject: Observable<String>

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            searchingText = it.getString(KEY_SEARCH)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleMovieSearching()
        search_toolbar.setText(searchingText)
    }

    private fun init() {
        movies_search_recycler_view.adapter = adapter.apply { addAll(listOf()) }

        val moviesList = searchingMovieslist.map {
            SearchingMoviesCardContainer(it)
        }
        movies_search_recycler_view.adapter = adapter.apply { addAll(moviesList) }
    }

    private fun handleMovieSearching() {
        filterSubject = Observable.create(ObservableOnSubscribe<String> { emitter ->
            search_edit_text.doAfterTextChanged{
                emitter.onNext(it.toString())
            }
        })

        filterSubject
            .subscribeOn(Schedulers.io())
            .map { it.trim() }
            .filter{it.length > 3}
            .debounce(500, TimeUnit.MILLISECONDS)
            .flatMap{ MovieApiClient.apiClient.searchMovie(it) }
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                searchingMovieslist.clear()
                adapter.clear()
                searchingMovieslist = it.results
                init()
                adapter.notifyDataSetChanged()
                Timber.d(it.results.size.toString()) }
            .subscribe({},{ error -> Timber.d(error)})
    }
}
