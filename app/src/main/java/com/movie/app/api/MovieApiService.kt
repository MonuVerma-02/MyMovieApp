package com.movie.app.api

import com.movie.app.model.MovieResponse
import com.movie.app.model.VideoResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {

    @GET("movie/popular")
    fun fetchPopularMoviesAsync(): Deferred<Response<MovieResponse>>

    @GET("search/movie")
    fun fetchMovieByQueryAsync(@Query("query") query: String): Deferred<Response<MovieResponse>>

    @GET("movie/now_playing")
    fun fetchCurrentMoviesAsync(): Deferred<Response<MovieResponse>>

    @GET("movie/top_rated")
    fun fetchTopRatedMoviesAsync(): Deferred<Response<MovieResponse>>

    @GET("movie/upcoming")
    fun fetchUpcomingMoviesAsync(): Deferred<Response<MovieResponse>>

    @GET("movie/{movie_id}/videos")
    fun fetchMoviesVideoAsync(@Path("movie_id") movieId: Int): Deferred<Response<VideoResponse>>
}