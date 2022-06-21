package com.example.androidbaseapp.domain.interactor

import com.example.androidbaseapp.domain.SingleUseCase
import com.example.androidbaseapp.data.repositories.model.BasicCountryModel
import com.example.androidbaseapp.domain.repositories.CovidDataRepository
import com.example.androidbaseapp.common.ResultWrapper
import javax.inject.Inject

class GetLocalBasicCountriesUseCase @Inject constructor(
    private val covidDataRepository: CovidDataRepository,
) : SingleUseCase<ResultWrapper<List<BasicCountryModel>>> {
    override suspend fun execute(): ResultWrapper<List<BasicCountryModel>> {
        return covidDataRepository.getLocalBasicCountries()
    }
}