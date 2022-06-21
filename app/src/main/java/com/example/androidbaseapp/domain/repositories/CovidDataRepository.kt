package com.example.androidbaseapp.domain.repositories

import com.example.androidbaseapp.data.repositories.model.BasicCountryModel
import com.example.androidbaseapp.data.repositories.model.DetailCountryModel
import com.example.androidbaseapp.data.repositories.model.LoadingKeyModel
import com.example.androidbaseapp.data.repositories.model.WorldWipModel
import com.example.androidbaseapp.common.ResultWrapper
import com.example.androidbaseapp.common.types.KeyType

interface CovidDataRepository {
    /*Remote*/
    suspend fun getRemoteBasicCountries(): ResultWrapper<List<BasicCountryModel>>

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
    suspend fun getLocalLoadingKeyPage(
        dataId: Int,
        keyType: Int = KeyType.KEY_COVID_LOADING_TYPE.value
    ): ResultWrapper<LoadingKeyModel>

    suspend fun insertLocalLoadingKey(
        data: LoadingKeyModel,
        keyType: Int = KeyType.KEY_COVID_LOADING_TYPE.value
    )

    suspend fun clearLocalLoadingKeys(keyType: Int = KeyType.KEY_COVID_LOADING_TYPE.value)
}