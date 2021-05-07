package ru.androidschool.intensiv.ui.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.feed_header.*
import kotlinx.android.synthetic.main.fragment_search.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.Movie
import ru.androidschool.intensiv.data.MovieResponse
import ru.androidschool.intensiv.ui.feed.FeedFragment.Companion.KEY_SEARCH
import timber.log.Timber


class SearchFragment : Fragment(R.layout.fragment_search) {

    private var searchingText: String? = null
    private var searchingMovieslist = mutableListOf<Movie>()

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

    private fun handleMovieSearching(){
        search_toolbar.getSearchingMovies()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { setInProgress(true) }
            .doFinally { setInProgress(false) }
            .subscribe({result -> setData(result)},{ error -> Timber.e(error)})
    }

    private fun init() {
        movies_search_recycler_view.adapter = adapter.apply { addAll(listOf()) }

        val moviesList = searchingMovieslist.map {
            SearchingMoviesCardContainer(it)
        }
        movies_search_recycler_view.adapter = adapter.apply { addAll(moviesList) }
    }



    private fun setData(movies : MovieResponse){
        searchingMovieslist.clear()
        adapter.clear()
        searchingMovieslist = movies.results
        init()
        adapter.notifyDataSetChanged()
        setInProgress(false)
    }

    private fun setInProgress(inProgress: Boolean){
        if (inProgress){
            search_fragment_loader.visibility = View.VISIBLE
            movies_search_recycler_view.visibility = View.GONE
        } else {
            search_fragment_loader.visibility = View.GONE
            movies_search_recycler_view.visibility = View.VISIBLE
        }
    }
}
