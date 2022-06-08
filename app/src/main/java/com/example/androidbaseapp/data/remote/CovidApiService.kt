package com.example.androidbaseapp.data.remote

import com.example.androidbaseapp.data.remote.entity.CovidDetailCountryResult
import com.example.androidbaseapp.data.remote.entity.CovidBasicCountryResult
import com.example.androidbaseapp.data.remote.entity.CovidWorldWipResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CovidApiService {

    @GET("countries")
    suspend fun getCountries(): List<CovidBasicCountryResult>

    @GET("live/country/{countryName}/status/{status}/date/{date}")
    suspend fun getLiveCountryByStatusFromDate(
        @Path("countryName") countryName: String,
        @Path("status") status: String,
        @Path("date") date: String
    ): List<CovidDetailCountryResult>

    @GET("world")
    suspend fun getWorkWip(
        @Query("from") startDate: String,
        @Query("to") endDate: String
    ): List<CovidWorldWipResult>

}