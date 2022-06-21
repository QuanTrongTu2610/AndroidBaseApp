package com.example.androidbaseapp.data.remote.http

import android.content.Context
import com.example.androidbaseapp.common.connectivity.NetworkUtils
import com.example.androidbaseapp.common.exceptions.NetworkConnectionException.NoConnectivityException
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * Custom Application Interceptor for handling network connection
 * */
class NetworkConnectionInterceptor constructor(
    private val context: Context?
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val isNetworkConnected = NetworkUtils.isInternetAvailable(context)
        if (!isNetworkConnected) throw NoConnectivityException("No Internet")
        val builder: Request.Builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }
}