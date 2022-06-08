package com.example.androidbaseapp.domain.interactor

import com.example.androidbaseapp.domain.SingleUseCase
import com.example.androidbaseapp.domain.model.BasicCountryModel
import com.example.androidbaseapp.domain.repositories.CovidDataRepository
import com.example.androidbaseapp.utils.Logger
import com.example.androidbaseapp.utils.ResultWrapper
import javax.inject.Inject

class GetLocalBasicCountriesUseCase @Inject constructor(
    private val covidDataRepository: CovidDataRepository,
) : SingleUseCase<ResultWrapper<List<BasicCountryModel>>> {
    override suspend fun execute(): ResultWrapper<List<BasicCountryModel>> {
        return try {
            covidDataRepository
                .getLocalBasicCountries()
                .let {
                    if (it is ResultWrapper.Success) {
                        val output = it.data
                        Logger.d("result: $output")
                        ResultWrapper.Success(output)
                    } else {
                        ResultWrapper.Error((it as ResultWrapper.Error).exception)
                    }
                }
        } catch (e: Exception) {
            Logger.e("error: ${e.message}")
            ResultWrapper.Error(e)
        }
    }
}