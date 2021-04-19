package ru.androidschool.intensiv.network

import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.androidschool.intensiv.BuildConfig
import ru.androidschool.intensiv.data.ActorsResponse
import ru.androidschool.intensiv.data.MovieDetails
import ru.androidschool.intensiv.data.MovieResponse
import ru.androidschool.intensiv.data.TvShowResponse

interface MovieApiInterface {

    @GET("movie/now_playing")
    fun getNowPlayingMovies(@Query("api_key") apiKey: String = BuildConfig.THE_MOVIE_DATABASE_API): Observable<MovieResponse>

    @GET("movie/upcoming")
    fun getUpcomingMovies(@Query("api_key") apiKey: String = BuildConfig.THE_MOVIE_DATABASE_API): Observable<MovieResponse>

    @GET("movie/popular")
    fun getPopularMovies(@Query("api_key") apiKey: String = BuildConfig.THE_MOVIE_DATABASE_API): Observable<MovieResponse>

    @GET("tv/popular")
    fun getPopularTvShows(@Query("api_key") apiKey: String = BuildConfig.THE_MOVIE_DATABASE_API): Observable<TvShowResponse>

    @GET("movie/{movie_id}/credits")
    fun getActors(@Path("movie_id") movieId: Int, @Query("api_key") apiKey: String = BuildConfig.THE_MOVIE_DATABASE_API): Observable<ActorsResponse>

    @GET("movie/{movie_id}")
    fun getMoviesDetails(@Path("movie_id") movieId: Int, @Query("api_key") apiKey: String = BuildConfig.THE_MOVIE_DATABASE_API): Observable<MovieDetails>

    @GET("search/movie")
    fun searchMovie(@Query("query") title: String, @Query("api_key") apiKey: String = BuildConfig.THE_MOVIE_DATABASE_API): Observable<MovieResponse>
}
