package com.example.androidbaseapp.common.connectivity

import android.content.Context

/**
 * This object provides some until functions related to networking
 * */
object NetworkUtils {

    private fun getNetworkConnection(
        context: Context?
    ): NetworkConnection {
        return NetworkConnectionImpl(context)
    }

    fun isInternetAvailable(context: Context?): Boolean {
        val networkType = getNetworkConnection(context)
            .netWorkStatus()
            .networkConnectionType
        return networkType != NetworkConnectionType.NONE
    }

}
