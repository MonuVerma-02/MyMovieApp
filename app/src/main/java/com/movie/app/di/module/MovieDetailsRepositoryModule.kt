package com.movie.app.di.module

import com.movie.app.api.MovieApiService
import com.movie.app.repository.MovieDetailsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class MovieDetailsRepositoryModule {
    @Provides
    fun provideViewModel(movieApiService: MovieApiService) = MovieDetailsRepository(movieApiService)
}