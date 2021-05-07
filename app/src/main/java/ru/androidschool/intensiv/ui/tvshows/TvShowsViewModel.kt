package ru.androidschool.intensiv.ui.tvshows

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.androidschool.intensiv.data.DataLoadingState
import ru.androidschool.intensiv.data.TvShow
import ru.androidschool.intensiv.domain.network.MovieApiClient
import timber.log.Timber

class TvShowsViewModel(private val repository: MovieApiClient) : ViewModel() {

    val tvShowsLiveData = MutableLiveData<List<TvShow>>()
    val tvShowsLoadingStateLiveDate = MutableLiveData<DataLoadingState>()
    val compositeDisposable = CompositeDisposable()

    init {
        getTvShows()
    }

    private fun getTvShows(){
        compositeDisposable.add(
            MovieApiClient.apiClient.getPopularTvShows()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { tvShowsLoadingStateLiveDate.value = DataLoadingState.LOADING }
                .subscribe({ result ->
                    tvShowsLiveData.value = result.results
                    tvShowsLoadingStateLiveDate.value = DataLoadingState.LOADED
                }, {error ->
                    tvShowsLoadingStateLiveDate.value = DataLoadingState.ERROR
                    Timber.e(error)  })
        )
    }

}