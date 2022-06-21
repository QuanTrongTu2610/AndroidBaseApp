package com.example.androidbaseapp.data.remote.http

import com.example.androidbaseapp.NetworkConfig
import com.example.androidbaseapp.common.Logger
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Custom Application Interceptor for handling network request token
 * */
class NetworkAuthenticationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain
            .request()
            .newBuilder()
            .addHeader("Authorization", NetworkConfig.NEWSPAPER_API_TOKEN)
            .build()
        Logger.d("newRequest: $newRequest")
        return chain.proceed(newRequest);
    }
}