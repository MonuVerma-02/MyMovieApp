package com.movie.app.di.module

import com.movie.app.repository.MovieDetailsRepository
import com.movie.app.ui.moviesdetails.MovieDetailsViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class MovieDetailsViewModelFactoryModule {

    @Provides
    fun provideDetailsViewModelFactory(movieDetailsRepository: MovieDetailsRepository) =
        MovieDetailsViewModelFactory(movieDetailsRepository)
}