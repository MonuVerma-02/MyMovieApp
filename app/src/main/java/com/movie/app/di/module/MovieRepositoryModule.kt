package com.movie.app.di.module

import com.movie.app.api.MovieApiService
import com.movie.app.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MovieRepositoryModule {

    @Provides
    @Singleton
    fun provideMovieRepository(movieApiService: MovieApiService) = MovieRepository(movieApiService)
}
