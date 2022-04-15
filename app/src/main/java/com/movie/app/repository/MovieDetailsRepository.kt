package com.movie.app.repository

import com.movie.app.model.Video
import com.movie.app.api.MovieApiService
import javax.inject.Inject

class MovieDetailsRepository @Inject constructor(private val movieApiService: MovieApiService) {
    suspend fun fetchMoviesVideoById(movieId: Int): List<Video>? {
        val response = movieApiService.fetchMoviesVideoAsync(movieId).await()
        return if (response.isSuccessful) {
            response.body()?.video
        } else {
            throw Exception()
        }
    }
}