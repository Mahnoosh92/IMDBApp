package com.example.network.di

import com.example.network.datasource.DefaultMovieRemoteDatasource
import com.example.network.datasource.MovieRemoteDatasource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DatasourceModule {
    @Binds
    @Singleton
    abstract fun bindMovieRemoteDatasource(impl: DefaultMovieRemoteDatasource): MovieRemoteDatasource
}
