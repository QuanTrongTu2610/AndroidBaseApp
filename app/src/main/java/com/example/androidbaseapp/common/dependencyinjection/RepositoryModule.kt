package com.example.androidbaseapp.common.dependencyinjection

import com.example.androidbaseapp.domain.repositories.CovidDataRepository
import com.example.androidbaseapp.data.repositories.CovidDataRepositoryImpl
import com.example.androidbaseapp.data.repositories.NewspaperRepositoryImpl
import com.example.androidbaseapp.domain.repositories.NewspaperRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun provideCovidRepository(
        covidDataRepositoryImpl: CovidDataRepositoryImpl
    ): CovidDataRepository

    @Binds
    @Singleton
    abstract fun provideNewspaperRepository(
        newspaperRepositoryImpl: NewspaperRepositoryImpl
    ): NewspaperRepository
}