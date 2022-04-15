package com.movie.app.ui.moviesdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.movie.app.repository.MovieDetailsRepository

class MovieDetailsViewModelFactory(private val repository: MovieDetailsRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MovieDetailsViewModel(repository) as T
    }
}