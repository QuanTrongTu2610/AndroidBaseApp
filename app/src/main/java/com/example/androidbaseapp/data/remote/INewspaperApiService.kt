package com.example.androidbaseapp.data.remote

import com.example.androidbaseapp.data.remote.entity.NewspaperResult
import retrofit2.http.GET
import retrofit2.http.Query

interface INewspaperApiService {
    @GET("everything")
    suspend fun getNewspaperByKeyWord(
        @Query("q") keyWord: String,
        @Query("page") page: String,
        @Query("pageSize") pageSize: String
    ): NewspaperResult
}