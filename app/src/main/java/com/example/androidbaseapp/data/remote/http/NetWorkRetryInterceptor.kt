package com.example.androidbaseapp.data.remote.http

import com.example.androidbaseapp.common.exceptions.NetworkConnectionException.TooManyRequestException
import com.example.androidbaseapp.common.Logger
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * Custom Application Interceptor for network fetching retry
 * */
class NetWorkRetryInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var response = chain.proceed(chain.request().newBuilder().build())
        var tryCount = 0
        if (!response.isSuccessful && response.code == 429) {
            throw TooManyRequestException(response.message)
        }
        while (!response.isSuccessful && tryCount < 3) {
            Logger.d("intercept", "Request is not successful - $tryCount");
            tryCount++;
            // retry the request
            val builder: Request.Builder = chain.request().newBuilder()
            response = chain.proceed(builder.build())
        }
        return response
    }
}