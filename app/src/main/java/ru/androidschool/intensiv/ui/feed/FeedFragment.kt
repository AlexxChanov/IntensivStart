package ru.androidschool.intensiv.ui.feed

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.feed_fragment.*
import kotlinx.android.synthetic.main.feed_header.*
import kotlinx.android.synthetic.main.search_toolbar.view.*
import retrofit2.Call
import retrofit2.Response
import ru.androidschool.intensiv.R
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
        getPopularMovies()
        getUpcomingMovies()
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

        // Используя Мок-репозиторий получаем фэйковый список фильмов
        val moviesList = listOf(
            MainCardContainer(
                R.string.recommended,
                popularMoviesList.map {
                    MovieItem(it) { movie ->
                        openMovieDetails(
                            movie
                        )
                    }
                }.toList()
            )
        )

        movies_recycler_view.adapter = adapter.apply { addAll(moviesList) }

        // Используя Мок-репозиторий получаем фэйковый список фильмов
        // Чтобы отобразить второй ряд фильмов
        val newMoviesList = listOf(
            MainCardContainer(
                R.string.upcoming,
                upcomingMoviesList.map {
                    MovieItem(it) { movie ->
                        openMovieDetails(movie)
                    }
                }.toList()
            )
        )

        adapter.apply { addAll(newMoviesList) }
    }

    private fun getPlayingMovies() {
        val call: retrofit2.Call<MovieResponse> = MovieApiClient.apiClient.getNowPlayingMovies()

        call.enqueue(object : retrofit2.Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful) {
                    playingMoviesList = response.body()?.results!!
                    initRecyclers()
                }
                Timber.d(response.message())
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Timber.d("Error ${t.message}")
            }

        })
    }

    private fun getPopularMovies() {
        val call: retrofit2.Call<MovieResponse> = MovieApiClient.apiClient.getPopularMovies()

        call.enqueue(object : retrofit2.Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful) {
                    popularMoviesList = response.body()?.results!!
                    initRecyclers()
                }
                Timber.d(response.message())
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Timber.d("Error ${t.message}")
            }

        })
    }

    private fun getUpcomingMovies() {
        val call: retrofit2.Call<MovieResponse> = MovieApiClient.apiClient.getUpcomingMovies()

        call.enqueue(object : retrofit2.Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful) {
                    upcomingMoviesList = response.body()?.results!!
                    initRecyclers()
                }
                Timber.d(response.message())
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Timber.d("Error ${t.message}")
            }

        })
    }

    private fun openMovieDetails(movie: Movie) {
//        val bundle = Bundle()
//        bundle.putParcelable(KEY_TITLE, movie)
//        findNavController().navigate(R.id.movie_details_fragment, bundle, options)
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
