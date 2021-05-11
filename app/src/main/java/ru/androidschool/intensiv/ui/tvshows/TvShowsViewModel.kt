package ru.androidschool.intensiv.ui.tvshows

import androidx.lifecycle.LiveData
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

   private val _tvShowsLiveData = MutableLiveData<List<TvShow>>()
    val tvShowsLiveData : LiveData<List<TvShow>>
        get() = _tvShowsLiveData


    private val _tvShowsLoadingStateLiveDate = MutableLiveData<DataLoadingState>()
    val tvShowsLoadingStateLiveDate : LiveData<DataLoadingState>
        get() = _tvShowsLoadingStateLiveDate

    val compositeDisposable = CompositeDisposable()

    init {
        getTvShows()
    }

    private fun getTvShows(){
        compositeDisposable.add(
            MovieApiClient.apiClient.getPopularTvShows()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { _tvShowsLoadingStateLiveDate.value = DataLoadingState.LOADING }
                .subscribe({ result ->
                    _tvShowsLiveData.value = result.results
                    _tvShowsLoadingStateLiveDate.value = DataLoadingState.LOADED
                }, {error ->
                    _tvShowsLoadingStateLiveDate.value = DataLoadingState.ERROR
                    Timber.e(error)  })
        )
    }

}