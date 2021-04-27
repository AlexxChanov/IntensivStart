package ru.androidschool.intensiv.ui.movie_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.movie_details_fragment.*
import ru.androidschool.intensiv.MovieFinderApp
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.*
import ru.androidschool.intensiv.database.LikedMovie
import ru.androidschool.intensiv.network.MovieApiClient
import ru.androidschool.intensiv.ui.namesStringBuilder
import ru.androidschool.intensiv.ui.setImage
import timber.log.Timber

private const val ARG_PARAM1 = "title"

class MovieDetailsFragment : Fragment() {

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    private var currentMovie: Movie? = null
    private lateinit var moviesDetails: MovieDetails
    private var actorsList = mutableListOf<Actor>()
    private val database = MovieFinderApp.instance?.getDatabase()
    private val dao = database?.getLikedMovieDao()
    private var isSaved: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            currentMovie = it.getParcelable<Movie>(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.movie_details_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        getDetailsAndActors()
    }

    private fun init() {
        currentMovie?.let { checkIfSaved(it) }

        movie_details_like_iv.setOnClickListener {
            if (isSaved != null){
                if (isSaved!!) {
                    setLiked(!isSaved!!)
                    currentMovie?.let { movie -> deleteDataFromDB(movie) }
                } else {
                    setLiked(!isSaved!!)
                    currentMovie?.let { movie -> writeToDb(movie) }
                }
            }
        }

    }
    private fun setData(result: DetailsFragmentResultsContainer) {
        moviesDetails = result.movieDetails
        actorsList.addAll(result.actors)

        movie_details_title.text = currentMovie?.title
        currentMovie?.rating?.let {
            movie_details_rating_bar.rating = it
        }
        movie_details_description.text = moviesDetails.overview

        currentMovie?.poster?.let { setImage(it, movie_details_iv) }
        movie_details_year_tv.text = currentMovie?.releaseDate
        movie_details_studio_tv.text = namesStringBuilder(moviesDetails.productionCompanies.map { it.name })
        movie_details_genre_tv.text = namesStringBuilder(moviesDetails.genres.map { it.name })
        initActorsRecyclerView()
    }

    private fun initActorsRecyclerView() {
        movie_detail_actors_rv.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        movie_detail_actors_rv.adapter = adapter.apply { addAll(listOf()) }

        actorsList.map {
            adapter.apply { addAll(listOf(ActorsCardContainer(it))) }
        }
    }

    private fun getDetailsAndActors(){
        currentMovie?.id?.let { id ->
            Observable.zip(MovieApiClient.apiClient.getMoviesDetails(id), MovieApiClient.apiClient.getActors(id),
                BiFunction<MovieDetails, ActorsResponse, DetailsFragmentResultsContainer>{movieDetails, actorsResponse -> DetailsFragmentResultsContainer(movieDetails, actorsResponse.cast)})
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { setInProgress(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { setInProgress(false) }
                .subscribe({ result -> setData(result) }, {error -> Timber.e(error)})
        }
    }

    private fun setInProgress(inProgress: Boolean){
        if (inProgress){
            movies_details_loader.visibility = View.VISIBLE
            movie_detail_actors_rv.visibility = View.GONE
        } else {
            movies_details_loader.visibility = View.GONE
            movie_detail_actors_rv.visibility = View.VISIBLE
        }
    }

     private fun checkIfSaved(movie: Movie) {
       Observable.just(dao?.getLikedById(movie.id))
           .subscribeOn(Schedulers.io())
           .observeOn(AndroidSchedulers.mainThread())
           .subscribe({ result -> handleCheckingResult(result) }, { error -> })
        }

    private fun handleCheckingResult(likedMovie: LikedMovie?){
        if (likedMovie == null){
            isSaved = false
            setLiked(false)
        } else {
            isSaved = true
            setLiked(true)
        }
    }

    private fun setLiked(isLiked: Boolean){
        if (isLiked){
            movie_details_like_iv.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_like_filled))
        } else {
            movie_details_like_iv.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_like))
        }
    }

    private fun deleteDataFromDB(movie: Movie) {
            Observable.just(dao?.getLikedById(movie.id))
                .subscribeOn(Schedulers.io())
                .doOnNext {
                    if (it != null) {
                        dao?.delete(it)
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result -> }, { error ->})
    }

    private fun writeToDb(movie: Movie) {
        val likedMovie  = LikedMovie(
        0, movie.id, movie.title.toString(), movie.voteAverage, movie.posterPath, movie.adult, movie.overview, movie.releaseDate, movie.genreIds, movie.originalTitle, movie.originalLanguage, movie.backdropPath, movie.popularity, movie.voteCount, movie.video)

        Observable.just(dao?.insertLikedMovie(likedMovie))
            .subscribeOn(Schedulers.io())
            .subscribe()
    }
}
