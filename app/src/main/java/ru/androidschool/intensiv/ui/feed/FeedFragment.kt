package ru.androidschool.intensiv.ui.feed

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.feed_fragment.*
import kotlinx.android.synthetic.main.feed_header.*
import kotlinx.android.synthetic.main.search_toolbar.view.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.FeedFragmentDataContainer
import ru.androidschool.intensiv.data.Movie
import ru.androidschool.intensiv.data.MovieResponse
import ru.androidschool.intensiv.network.MovieApiClient
import ru.androidschool.intensiv.ui.afterTextChanged
import timber.log.Timber

private const val TAG = "FeedFragment"

class FeedFragment : Fragment() {

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }
    private var playingMoviesList: List<Movie> = listOf()
    private var popularMoviesList: List<Movie> = listOf()
    private var upcomingMoviesList: List<Movie> = listOf()
    lateinit var requestsContainer: FeedFragmentDataContainer

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
        zipRequestsFunction3()
    }

    private fun initRecyclers() {
        // Добавляем recyclerView
        movies_recycler_view.layoutManager = LinearLayoutManager(context)
        movies_recycler_view.adapter = adapter.apply { addAll(listOf()) }

        search_toolbar.search_edit_text.afterTextChanged {
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

    private fun zipRequestsFunction3(){
        Observable.zip(MovieApiClient.apiClient.getUpcomingMovies(), MovieApiClient.apiClient.getPopularMovies(), MovieApiClient.apiClient.getNowPlayingMovies(),
        Function3<MovieResponse, MovieResponse, MovieResponse, FeedFragmentDataContainer>{
            upcoming, popular, nowPlaying ->  FeedFragmentDataContainer(upcoming.results, popular.results, nowPlaying.results) })
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { setInProgress(true) }
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { setInProgress(false) }
            .subscribe({result -> initResults(result)}, { error -> Timber.e(error)})
    }

    private fun initResults(results: FeedFragmentDataContainer){
        upcomingMoviesList = results.upcomingMovies
        popularMoviesList =  results.popularMovies
        playingMoviesList =  results.playingMovies
        initRecyclers()
    }

    private fun setInProgress(inProgress: Boolean){
        if (inProgress){
            feed_fragment_loader.visibility = View.VISIBLE
            movies_recycler_view.visibility = View.GONE
        } else {
            feed_fragment_loader.visibility = View.GONE
            movies_recycler_view.visibility = View.VISIBLE
        }
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
