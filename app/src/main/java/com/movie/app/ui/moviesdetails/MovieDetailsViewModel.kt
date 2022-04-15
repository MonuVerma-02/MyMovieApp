package com.movie.app.ui.moviesdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movie.app.common.MovieLoadingState
import com.movie.app.model.Video
import com.movie.app.repository.MovieDetailsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieDetailsViewModel(private val movieDetailsRepository: MovieDetailsRepository) : ViewModel() {

    val videoLoadingStatusLiveData = MutableLiveData<MovieLoadingState>()

    fun fetchMovieVideosById(movieId: Int): LiveData<List<Video>> {
        val moviesVideo = MutableLiveData<List<Video>>()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val videos = movieDetailsRepository.fetchMoviesVideoById(movieId)
                moviesVideo.postValue(videos!!)
                videoLoadingStatusLiveData.postValue(MovieLoadingState.LOADED)
            } catch (e: Exception) {
                videoLoadingStatusLiveData.postValue(MovieLoadingState.ERROR)
            }
        }
        return moviesVideo
    }
}