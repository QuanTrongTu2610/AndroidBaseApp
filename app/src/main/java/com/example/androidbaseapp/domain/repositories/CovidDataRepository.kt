package com.example.androidbaseapp.domain.repositories

import androidx.paging.PagingData
import com.example.androidbaseapp.domain.model.BasicCountryModel
import com.example.androidbaseapp.domain.model.DetailCountryModel
import com.example.androidbaseapp.domain.model.LoadingKeyModel
import com.example.androidbaseapp.domain.model.WorldWipModel
import com.example.androidbaseapp.utils.ResultWrapper
import kotlinx.coroutines.flow.Flow

interface CovidDataRepository {
    /*Remote*/
    suspend fun getRemoteBasicCountries(): ResultWrapper<List<BasicCountryModel>>

    suspend fun getRemoteDetailCountries(date: String): ResultWrapper<Flow<PagingData<DetailCountryModel>>>

    suspend fun getRemoteWorldWip(
        startDate: String,
        endDate: String
    ): ResultWrapper<List<WorldWipModel>>

    /*Local*/
    suspend fun clearLocalBasicCountries()

    suspend fun clearLocalDetailCountries()

    suspend fun insertLocalBasicCountries(data: List<BasicCountryModel>)

    suspend fun getLocalBasicCountries(): ResultWrapper<List<BasicCountryModel>>

    suspend fun getLocalBasicCountries(
        startId: Int,
        endId: Int
    ): ResultWrapper<List<BasicCountryModel>>

    suspend fun insertLocalDetailCountries(data: List<DetailCountryModel>)

    suspend fun countNumberOfBasicCountry(): Int

    /*Loading key*/
    suspend fun getLocalLoadingKeyPage(dataId: Int): ResultWrapper<LoadingKeyModel>

    suspend fun insertLocalLoadingKey(data: LoadingKeyModel)

    suspend fun clearLocalLoadingKeys()
}