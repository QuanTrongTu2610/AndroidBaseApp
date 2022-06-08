package com.example.androidbaseapp.domain.interactor

import com.example.androidbaseapp.data.exceptions.NetworkConnectionException.NoConnectivityException
import com.example.androidbaseapp.domain.SingleUseCase
import com.example.androidbaseapp.domain.model.BasicCountryModel
import com.example.androidbaseapp.domain.repositories.CovidDataRepository
import com.example.androidbaseapp.utils.Logger
import com.example.androidbaseapp.utils.ResultWrapper
import java.io.IOException
import javax.inject.Inject

class GetRemoteBasicCountriesUseCase @Inject constructor(
    private val covidDataRepository: CovidDataRepository,
) : SingleUseCase<ResultWrapper<List<BasicCountryModel>>>{
    override suspend fun execute(): ResultWrapper<List<BasicCountryModel>> {
        return try {
            covidDataRepository
                .getRemoteBasicCountries()
                .let {
                    if (it is ResultWrapper.Success) {
                        val output = it.data
                        ResultWrapper.Success(output)
                    } else {
                        ResultWrapper.Error((it as ResultWrapper.Error).exception)
                    }
                }
        } catch (e: IOException) {
            Logger.e("error: ${e.message}")
            ResultWrapper.Error(e)
        } catch (e: NoConnectivityException) {
            Logger.e("No internet connection")
            ResultWrapper.Error(e)
        }
    }
}