package com.example.androidbaseapp.data.remote

import com.example.androidbaseapp.NetworkConfig
import com.example.androidbaseapp.common.Logger
import com.example.androidbaseapp.data.remote.entity.CovidDetailCountryResult
import com.example.androidbaseapp.data.remote.entity.CovidBasicCountryResult
import com.example.androidbaseapp.data.remote.entity.CovidWorldWipResult
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * This class used to make remote api service become dynamic in project. It used to add client, interceptor, url
 * and create Endpoint Service
 * */
class CovidDynamicApiService @Inject constructor(
    private val retrofitBuilder: Retrofit.Builder
) {

    private var dynamicApiService: ICovidApiService? = null

    private var url: String = NetworkConfig.API_DOMAIN_DEFAULT
        set(value) {
            if (field != value) {
                Logger.d("Update api domain. Old url: $field, New url: $value")
                field = value
                dynamicApiService = null
            } else {
                Logger.d("Api domain update is same. Url: $value")
            }
        }

    private val apiService: ICovidApiService
        get() {
            if (dynamicApiService == null) {
                dynamicApiService = retrofitBuilder
                    .baseUrl(url)
                    .build()
                    .create(ICovidApiService::class.java)
            }
            return dynamicApiService!!
        }

    suspend fun getCountries(): List<CovidBasicCountryResult> =
        apiService.getCountries()

    suspend fun getLiveCountryByStatusFromDate(
        countryName: String,
        status: String,
        date: String,
    ): List<CovidDetailCountryResult> {
        return apiService.getLiveCountryByStatusFromDate(
            countryName = countryName,
            status = status,
            date = date
        )
    }

    suspend fun getWorkWip(
        startDate: String,
        endDate: String
    ): List<CovidWorldWipResult> = apiService.getWorkWip(
        startDate = startDate,
        endDate = endDate
    )

    fun recreateService() {
        dynamicApiService = null
    }

    fun updateApiDomain(url: String) {
        Logger.d("updateApiDomain: $url")
        this.url = url
    }
}
