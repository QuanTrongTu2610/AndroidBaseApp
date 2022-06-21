package com.example.androidbaseapp.data.remote

import com.example.androidbaseapp.NetworkConfig
import com.example.androidbaseapp.data.remote.entity.NewspaperResult
import com.example.androidbaseapp.common.Logger
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * This class used to make remote api service become dynamic in project. It used to add client, interceptor, url
 * and create Endpoint Service
 * */
class NewspaperDynamicApiService @Inject constructor(
    private val retrofitBuilder: Retrofit.Builder
) {
    private var dynamicApiService: INewspaperApiService? = null

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

    private val newspaperApiService: INewspaperApiService
        get() {
            if (dynamicApiService == null) {
                dynamicApiService = retrofitBuilder
                    .baseUrl(url)
                    .build()
                    .create(INewspaperApiService::class.java)
                dynamicApiService
            }
            return dynamicApiService!!
        }

    suspend fun getNewspaperByKeyWord(
        keyWord: String,
        pageSize: String,
        page: String
    ): NewspaperResult {
        return newspaperApiService.getNewspaperByKeyWord(keyWord, page, pageSize)
    }

    fun updateApiDomain(url: String) {
        Logger.d("updateApiDomain url: $url")
        this.url = url
    }

    fun recreateService() {
        dynamicApiService = null
    }
}