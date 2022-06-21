package com.example.androidbaseapp.common.dependencyinjection

import com.example.androidbaseapp.domain.interactor.GetLocalBasicCountriesUseCase
import com.example.androidbaseapp.domain.interactor.GetNewspaperUseCase
import com.example.androidbaseapp.domain.interactor.GetRemoteBasicCountriesUseCase
import com.example.androidbaseapp.domain.interactor.GetRemoteTotalWorldWipUseCase
import com.example.androidbaseapp.domain.interactor.SaveLocalBasicCountriesUseCase
import com.example.androidbaseapp.domain.repositories.CovidDataRepository
import com.example.androidbaseapp.domain.repositories.NewspaperRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class UseCaseModule {

    @Provides
    fun provideSpecificGetFromRemoteBasicCountriesDataUseCase(
        covidDataRepository: CovidDataRepository
    ): GetRemoteBasicCountriesUseCase {
        return GetRemoteBasicCountriesUseCase(covidDataRepository)
    }

    @Provides
    fun provideSpecificGetLocalBasicCountriesUseCase(
        covidDataRepository: CovidDataRepository
    ): GetLocalBasicCountriesUseCase {
        return GetLocalBasicCountriesUseCase(covidDataRepository)
    }

    @Provides
    fun provideSpecificSaveToLocalBasicCountriesDataUseCase(
        covidDataRepository: CovidDataRepository
    ): SaveLocalBasicCountriesUseCase {
        return SaveLocalBasicCountriesUseCase(covidDataRepository)
    }

    @Provides
    fun provideGetRemoteTotalWorldWipUseCase(
        covidDataRepository: CovidDataRepository
    ): GetRemoteTotalWorldWipUseCase {
        return GetRemoteTotalWorldWipUseCase(covidDataRepository)
    }

    @Provides
    fun provideGetNewspaperUseCase(
        newspaperRepository: NewspaperRepository
    ): GetNewspaperUseCase {
        return GetNewspaperUseCase(newspaperRepository)
    }
}