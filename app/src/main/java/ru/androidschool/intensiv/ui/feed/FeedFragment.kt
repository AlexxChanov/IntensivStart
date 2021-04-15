package ru.androidschool.intensiv.ui.feed

import android.os.Bundle
import android.view.*
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.feed_fragment.*
import kotlinx.android.synthetic.main.feed_header.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.search_toolbar.*
import kotlinx.android.synthetic.main.search_toolbar.view.*
import kotlinx.android.synthetic.main.tv_shows_fragment.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.Movie
import ru.androidschool.intensiv.network.MovieApiClient
import ru.androidschool.intensiv.ui.afterTextChanged
import ru.androidschool.intensiv.ui.tvshows.TvShowCardContainer
import timber.log.Timber
import java.util.concurrent.TimeUnit

private const val TAG = "FeedFragment"

class FeedFragment : Fragment() {

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }
    private var playingMoviesList: List<Movie> = listOf()
    private var popularMoviesList: List<Movie> = listOf()
    private var upcomingMoviesList: List<Movie> = listOf()

    lateinit var playingMoviesListCard: MainCardContainer
    lateinit var popularMoviesListCard: MainCardContainer
    lateinit var upcomingMoviesListCard: MainCardContainer

    private val options = navOptions {
        anim {
            enter = R.anim.slide_in_right
            exit = R.anim.slide_out_left
            popEnter = R.anim.slide_in_left
            popExit = R.anim.slide_out_right
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.feed_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getPlayingMovies()
    }

    private fun initRecyclers() {
        // Добавляем recyclerView
        movies_recycler_view.layoutManager = LinearLayoutManager(context)
        movies_recycler_view.adapter = adapter.apply { addAll(listOf()) }

        search_toolbar.search_edit_text.afterTextChanged {
            Timber.d(it.toString())
            if (it.toString().length > MIN_LENGTH) {
                openSearch(it.toString())
            }
        }

        popularMoviesListCard =
            MainCardContainer(
                R.string.popular,
                popularMoviesList.map {
                    MovieItem(it) { movie ->
                        openMovieDetails(
                            movie
                        )
                    }
                }.toList()
            )

        upcomingMoviesListCard =
            MainCardContainer(
                R.string.upcoming,
                upcomingMoviesList.map {
                    MovieItem(it) { movie ->
                        openMovieDetails(movie)
                    }
                }.toList()
            )

        playingMoviesListCard =
            MainCardContainer(
                R.string.now_playing,
                playingMoviesList.map {
                    MovieItem(it) { movie ->
                        openMovieDetails(
                            movie
                        )
                    }
                }.toList()
            )

        adapter.apply {
            addAll(
                listOf(
                    playingMoviesListCard,
                    upcomingMoviesListCard,
                    popularMoviesListCard
                )
            )
        }
    }

    private fun getPlayingMovies() {
        MovieApiClient.apiClient.getNowPlayingMovies()
            .subscribeOn(Schedulers.io())
            .map { playingMoviesList = it.results }
            .doOnError { error -> Timber.d("$error") }
            .doOnComplete { getPopularMovies() }
            .subscribe()
    }

    private fun getPopularMovies() {
        MovieApiClient.apiClient.getPopularMovies()
            .subscribeOn(Schedulers.io())
            .map { popularMoviesList = it.results }
            .doOnError { error -> Timber.d(error) }
            .doOnComplete { getUpcomingMovies() }
            .subscribe()
    }

    private fun getUpcomingMovies() {
        MovieApiClient.apiClient.getUpcomingMovies()
            .subscribeOn(Schedulers.io())
            .map { upcomingMoviesList = it.results }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { error -> Timber.d(error) }
            .doOnComplete { initRecyclers() }
            .subscribe()
    }

    private fun openMovieDetails(movie: Movie) {
        val bundle = Bundle()
        bundle.putParcelable(KEY_TITLE, movie)
        findNavController().navigate(R.id.movie_details_fragment, bundle, options)
    }

    private fun openSearch(searchText: String) {
        val bundle = Bundle()
        bundle.putString(KEY_SEARCH, searchText)
        findNavController().navigate(R.id.search_dest, bundle, options)
    }

    override fun onStop() {
        super.onStop()
        search_toolbar.clear()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    companion object {
        const val MIN_LENGTH = 3
        const val KEY_TITLE = "title"
        const val KEY_SEARCH = "search"
    }
}
