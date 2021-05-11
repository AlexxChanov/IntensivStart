package ru.androidschool.intensiv.ui.tvshows

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.tv_shows_fragment.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.*
import ru.androidschool.intensiv.domain.network.MovieApiClient
import java.lang.IllegalArgumentException

class TvShowsFragment : Fragment() {

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }
    private lateinit var tvShowsViewModel: TvShowsViewModel

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

        val tvShowsViewModelFactory = TvShowsViewModelFactory(MovieApiClient)

        tvShowsViewModel = ViewModelProvider(requireActivity(), tvShowsViewModelFactory).get(TvShowsViewModel::class.java)

        tvShowsViewModel.tvShowsLiveData.observe(requireActivity(), Observer { list ->
            initRecyclerView(list as MutableList<TvShow>)

        })
        tvShowsViewModel.tvShowsLoadingStateLiveDate.observe(requireActivity(), Observer {
            inProgress(it == DataLoadingState.LOADING)
        })
    }

    private fun initRecyclerView(tvShows: MutableList<TvShow>) {
        tv_shows_recyclerview.adapter = adapter.apply { addAll(listOf()) }

        val moviesList = tvShows.map {
            TvShowCardContainer(it)
        }
        tv_shows_recyclerview.adapter = adapter.apply { addAll(moviesList) }
    }

    private fun inProgress(inProgress: Boolean) {
        if (inProgress) {
            tv_shows_loader.visibility = View.VISIBLE
            tv_shows_recyclerview.visibility = View.GONE
        } else {
            tv_shows_loader.visibility = View.GONE
            tv_shows_recyclerview.visibility = View.VISIBLE
        }
    }
}

class TvShowsViewModelFactory(private val repository: MovieApiClient) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TvShowsViewModel::class.java)) {
            return TvShowsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
