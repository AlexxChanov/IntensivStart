package ru.androidschool.intensiv.ui.watchlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_watchlist.movies_recycler_view
import ru.androidschool.intensiv.MovieFinderApp
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.Movie
import ru.androidschool.intensiv.database.LikedMovie
import timber.log.Timber

class WatchlistFragment : Fragment() {

    val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }
    private val database = MovieFinderApp.instance?.getDatabase()
    private val dao = database?.getLikedMovieDao()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_watchlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movies_recycler_view.layoutManager = GridLayoutManager(context, 4)
        movies_recycler_view.adapter = adapter.apply { addAll(listOf()) }
        getLikedMovies()
    }

    private fun getLikedMovies() {
        dao?.let { dao ->
            dao.getAllLiked()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result -> handleMoviesResult(result) }, { error -> Timber.d(error.message) })
        }
    }

    private fun handleMoviesResult(result: List<LikedMovie>?) {
        result?.let {
            adapter.addAll(it.map {
                MoviePreviewItem(Movie(it.title, it.voteAverage, it.posterPath, it.adult, it.overview, it.releaseDate, it.genreIds, it.id.toInt(), it.originalTitle, it.originalLanguage, it.backdropPath, it.popularity, it.voteCount, it.video), { Timber.d("Movie clicked") })
            })
        }
    }
}
