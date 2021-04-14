package ru.androidschool.intensiv.ui.movie_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.movie_details_fragment.*
import retrofit2.Call
import retrofit2.Response
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.*
import ru.androidschool.intensiv.network.MovieApiClient
import ru.androidschool.intensiv.ui.genreStringBuilder
import ru.androidschool.intensiv.ui.setImage
import ru.androidschool.intensiv.ui.studiosStringBuilder
import timber.log.Timber

private const val ARG_PARAM1 = "title"

class MovieDetailsFragment : Fragment() {

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    private var currentMovie: Movie? = null
    private lateinit var moviesDetails: MovieDetails
    private var actorsList = mutableListOf<Actor>()

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
        getMoviesDetails()
    }

    private fun setData() {
        movie_details_title.text = currentMovie?.title
        currentMovie?.rating?.let {
            movie_details_rating_bar.rating = it
        }
        movie_details_description.text = moviesDetails.overview

        currentMovie?.poster?.let { setImage(it, movie_details_iv) }
        movie_details_year_tv.text = currentMovie?.releaseDate
        movie_details_studio_tv.text = studiosStringBuilder(moviesDetails.productionCompanies)
        movie_details_genre_tv.text = genreStringBuilder(moviesDetails.genres)
        initActorsRecyclerView()
    }

    private fun initActorsRecyclerView() {
        movie_detail_actors_rv.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        movie_detail_actors_rv.adapter = adapter.apply { addAll(listOf()) }

        actorsList.map {
            adapter.apply { addAll(listOf(ActorsCardContainer(it))) }
        }
    }

    private fun getMoviesDetails() {
        val call: retrofit2.Call<MovieDetails> = MovieApiClient.apiClient.getMoviesDetails(currentMovie!!.id)

        call.enqueue(object : retrofit2.Callback<MovieDetails> {
            override fun onResponse(call: Call<MovieDetails>, response: Response<MovieDetails>) {
                if (response.isSuccessful) {
                    moviesDetails = response.body()!!
                    setData()
                    getActors()
                }
                Timber.d(response.message())
            }

            override fun onFailure(call: Call<MovieDetails>, t: Throwable) {
                Timber.d("Error ${t.message}")
            }
        })
    }

    private fun getActors() {
        val call: retrofit2.Call<ActorsResponse> = MovieApiClient.apiClient.getActors(currentMovie!!.id)

        call.enqueue(object : retrofit2.Callback<ActorsResponse> {
            override fun onResponse(call: Call<ActorsResponse>, response: Response<ActorsResponse>) {
                if (response.isSuccessful) {
                    actorsList.addAll(response.body()!!.cast)
                    setData()
                }
                Timber.d(response.message())
            }

            override fun onFailure(call: Call<ActorsResponse>, t: Throwable) {
                Timber.d("Error ${t.message}")
            }
        })
    }
}
