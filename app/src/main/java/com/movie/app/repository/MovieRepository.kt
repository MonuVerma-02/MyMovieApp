package com.movie.app.repository

import com.movie.app.model.Movie
import com.movie.app.api.MovieApiService
import com.movie.app.di.module.MovieApiModule
import javax.inject.Inject

class MovieRepository @Inject constructor(private val movieApiService: MovieApiService) {

    suspend fun fetchPopularMovies(): List<Movie>? {
        val response = movieApiService.fetchPopularMoviesAsync().await()
        return if (response.isSuccessful) {
            response.body()?.movies
        } else {
            throw Exception()
        }
    }

    suspend fun fetchMovieByQuery(searchText: String): List<Movie>? {
        val response = movieApiService.fetchMovieByQueryAsync(searchText).await()
        return if (response.isSuccessful) {
            response.body()?.movies
        } else {
            throw Exception()
        }
    }

    suspend fun fetchCurrentMovies(): List<Movie>? {
        val response = movieApiService.fetchCurrentMoviesAsync().await()
        return if (response.isSuccessful) {
            response.body()?.movies
        } else {
            throw Exception()
        }
    }

    suspend fun fetchTopRatedMovies(): List<Movie>? {
        val response = movieApiService.fetchTopRatedMoviesAsync().await()
        return if (response.isSuccessful) {
            response.body()?.movies
        } else {
            throw Exception()
        }
    }

    suspend fun fetchUpcomingMovies(): List<Movie>? {
        val response = movieApiService.fetchUpcomingMoviesAsync().await()
        return if (response.isSuccessful) {
            response.body()?.movies
        } else {
            throw Exception()
        }
    }

}