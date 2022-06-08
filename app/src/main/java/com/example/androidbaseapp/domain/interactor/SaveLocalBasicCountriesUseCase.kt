package com.example.androidbaseapp.domain.interactor

import com.example.androidbaseapp.domain.SingleUseCaseWithParameter
import com.example.androidbaseapp.domain.model.BasicCountryModel
import com.example.androidbaseapp.domain.repositories.CovidDataRepository
import com.example.androidbaseapp.utils.Logger
import com.example.androidbaseapp.utils.ResultWrapper
import java.lang.Exception
import javax.inject.Inject

class SaveLocalBasicCountriesUseCase @Inject constructor(
    private val covidDataRepository: CovidDataRepository
) : SingleUseCaseWithParameter<List<BasicCountryModel>, ResultWrapper<Boolean>> {
    override suspend fun execute(parameter: List<BasicCountryModel>?): ResultWrapper<Boolean> {
        // sort country before saving
        val input = parameter?.sortedBy { it.abbreviation }
        return try {
            if (input != null) covidDataRepository.insertLocalBasicCountries(input)
            ResultWrapper.Success(true)
        } catch (e: Exception) {
            Logger.e("Undefined Exception: ${e.message}")
            ResultWrapper.Error(e)
        }
    }
}