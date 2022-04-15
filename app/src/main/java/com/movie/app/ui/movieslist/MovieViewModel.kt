package com.movie.app.ui.movieslist

import androidx.lifecycle.*
import com.movie.app.common.MovieLoadingState
import com.movie.app.model.Movie
import com.movie.app.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class MovieViewModel @Inject constructor(private val movieRepository: MovieRepository) :
    ViewModel() {

    val movieLoadingStatusLiveData = MutableLiveData<MovieLoadingState>()
    private val popularMoviesLiveData = MutableLiveData<List<Movie>>()

    private val currentMovieLiveData = MutableLiveData<List<Movie>>()
    private val topRateMovieLiveData = MutableLiveData<List<Movie>>()
    private val upcomingMovieLiveData = MutableLiveData<List<Movie>>()

    private var searchMovieLiveData: LiveData<List<Movie>>
    private val searchTextFieldLiveData = MutableLiveData<String>()
    private var searchJob: Job? = null

    val movieMediatorLiveData = MediatorLiveData<List<Movie>>()

    val navSubHeaderLiveData = MutableLiveData<String>()

    init {
        searchMovieLiveData = Transformations.switchMap(searchTextFieldLiveData) {
            fetchMovieByQuery(it)
        }

        movieMediatorLiveData.addSource(popularMoviesLiveData) {
            movieMediatorLiveData.value = it
        }
        movieMediatorLiveData.addSource(searchMovieLiveData) {
            movieMediatorLiveData.value = it
        }
        movieMediatorLiveData.addSource(currentMovieLiveData) {
            movieMediatorLiveData.value = it
        }
        movieMediatorLiveData.addSource(topRateMovieLiveData) {
            movieMediatorLiveData.value = it
        }
        movieMediatorLiveData.addSource(upcomingMovieLiveData) {
            movieMediatorLiveData.value = it
        }
    }

    fun onFragmentReady() {
        if (popularMoviesLiveData.value.isNullOrEmpty()) {
            fetchPopularMovies()
        }
    }

    fun onSearchQuery(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if (query.length > 2) {
                searchTextFieldLiveData.value = query
            }
        }
    }

    fun fetchPopularMovies() {
        movieLoadingStatusLiveData.value = MovieLoadingState.LOADING
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val movies = movieRepository.fetchPopularMovies()
                popularMoviesLiveData.postValue(movies!!)
                movieLoadingStatusLiveData.postValue(MovieLoadingState.LOADED)
            } catch (e: Exception) {
                movieLoadingStatusLiveData.postValue(MovieLoadingState.ERROR)
            }
        }
    }

    private fun fetchMovieByQuery(query: String): LiveData<List<Movie>> {
        movieLoadingStatusLiveData.value = MovieLoadingState.LOADING
        val movieList = MutableLiveData<List<Movie>>()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val movies = movieRepository.fetchMovieByQuery(query)
                movieList.postValue(movies!!)
                movieLoadingStatusLiveData.postValue(MovieLoadingState.LOADED)
            } catch (e: Exception) {
                movieLoadingStatusLiveData.postValue(MovieLoadingState.ERROR)
            }
        }
        return movieList
    }

    fun fetchCurrentMovies() {
        movieLoadingStatusLiveData.value = MovieLoadingState.LOADING
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val movies = movieRepository.fetchCurrentMovies()
                currentMovieLiveData.postValue(movies!!)
                movieLoadingStatusLiveData.postValue(MovieLoadingState.LOADED)
            } catch (e: Exception) {
                movieLoadingStatusLiveData.postValue(MovieLoadingState.ERROR)
            }
        }
    }

    fun fetchTopRatedMovies() {
        movieLoadingStatusLiveData.value = MovieLoadingState.LOADING
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val movies = movieRepository.fetchTopRatedMovies()
                topRateMovieLiveData.postValue(movies!!)
                movieLoadingStatusLiveData.postValue(MovieLoadingState.LOADED)
            } catch (e: Exception) {
                movieLoadingStatusLiveData.postValue(MovieLoadingState.ERROR)
            }
        }
    }

    fun fetchUpcomingMovies() {
        movieLoadingStatusLiveData.value = MovieLoadingState.LOADING
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val movies = movieRepository.fetchUpcomingMovies()
                upcomingMovieLiveData.postValue(movies!!)
                movieLoadingStatusLiveData.postValue(MovieLoadingState.LOADED)
            } catch (e: Exception) {
                movieLoadingStatusLiveData.postValue(MovieLoadingState.ERROR)
            }
        }
    }

    fun setNavSubHeader(resourceId: String) {
        navSubHeaderLiveData.value = resourceId
    }

    override fun onCleared() {
        super.onCleared()
        searchJob?.cancel()
    }
}