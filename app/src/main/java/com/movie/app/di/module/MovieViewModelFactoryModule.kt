package com.movie.app.di.module

import com.movie.app.repository.MovieRepository
import com.movie.app.ui.movieslist.MovieViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class MovieViewModelFactoryModule {

    @Provides
    fun provideViewModelFactory(movieRepository: MovieRepository) =
        MovieViewModelFactory(movieRepository)
}