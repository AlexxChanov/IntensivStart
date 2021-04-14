package ru.androidschool.intensiv.ui.tvshows

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.tv_shows_fragment.*
import retrofit2.Call
import retrofit2.Response
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.*
import ru.androidschool.intensiv.network.MovieApiClient
import timber.log.Timber

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class TvShowsFragment : Fragment() {

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var popularTvShows: MutableList<TvShow>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
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

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TvShowsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun getPopularTvShows() {
        val call: retrofit2.Call<TvShowResponse> = MovieApiClient.apiClient.getPopularTvShows()

        call.enqueue(object : retrofit2.Callback<TvShowResponse> {
            override fun onResponse(call: Call<TvShowResponse>, response: Response<TvShowResponse>) {
                if (response.isSuccessful) {
                    popularTvShows = response.body()?.results!!
                    init()
                }
                Timber.d(response.message())
            }

            override fun onFailure(call: Call<TvShowResponse>, t: Throwable) {
                Timber.d("Error ${t.message}")
            }
        })
    }
}
