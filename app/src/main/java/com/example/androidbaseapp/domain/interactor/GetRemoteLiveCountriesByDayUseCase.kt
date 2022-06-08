package com.example.androidbaseapp.domain.interactor

import androidx.paging.PagingData
import com.example.androidbaseapp.domain.SingleUseCaseWithParameter
import com.example.androidbaseapp.domain.model.DetailCountryModel
import com.example.androidbaseapp.domain.repositories.CovidDataRepository
import com.example.androidbaseapp.utils.Logger
import com.example.androidbaseapp.utils.ResultWrapper
import kotlinx.coroutines.flow.Flow
import java.lang.Exception
import javax.inject.Inject

class GetRemoteLiveCountriesByDayUseCase @Inject constructor(
    private val covidDataRepository: CovidDataRepository,
) : SingleUseCaseWithParameter<String, ResultWrapper<Flow<PagingData<DetailCountryModel>>>> {
    override suspend fun execute(parameter: String?): ResultWrapper<Flow<PagingData<DetailCountryModel>>> {
        Logger.d("parameter: $parameter")
        return parameter?.let {
            covidDataRepository.getRemoteDetailCountries(it).let { result ->
                when (result) {
                    is ResultWrapper.Success -> result
                    else -> ResultWrapper.Error(Exception("query unsuccessful"))
                }
            }
        } ?: ResultWrapper.Error(Exception("query nothing"))
    }
}