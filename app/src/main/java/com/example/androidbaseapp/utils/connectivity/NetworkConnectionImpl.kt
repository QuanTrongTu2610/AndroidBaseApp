package com.example.androidbaseapp.utils.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.example.androidbaseapp.utils.Logger
import javax.inject.Inject

class NetworkConnectionImpl @Inject constructor(
    private val context: Context?
) : NetworkConnection {

    override fun netWorkStatus(): NetworkStatus {
        context ?: run {
            Logger.d("getConnectionType context: null")
            return NetworkStatus(networkConnectionType = NetworkConnectionType.NONE)
        }

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
                ?: run {
                    Logger.d("connectivityManager: null")
                    return NetworkStatus(networkConnectionType = NetworkConnectionType.NONE)
                }

        val networkConnectionType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            connectivityManager
                .getNetworkCapabilities(connectivityManager.activeNetwork)
                ?.let { networkCapabilities ->
                    when {
                        networkCapabilities.hasTransport(
                            NetworkCapabilities.TRANSPORT_WIFI
                        ) -> NetworkConnectionType.WIFI

                        networkCapabilities.hasTransport(
                            NetworkCapabilities.TRANSPORT_CELLULAR
                        ) -> NetworkConnectionType.CELLULAR

                        else -> NetworkConnectionType.NONE
                    }
                } ?: NetworkConnectionType.NONE
        } else {
            connectivityManager
                .activeNetworkInfo?.type?.let { type ->
                    when (type) {
                        ConnectivityManager.TYPE_WIFI -> NetworkConnectionType.WIFI

                        ConnectivityManager.TYPE_MOBILE -> NetworkConnectionType.CELLULAR

                        else -> NetworkConnectionType.NONE
                    }
                } ?: NetworkConnectionType.NONE
        }
        return NetworkStatus(networkConnectionType = networkConnectionType)
    }
}

/**
 * This class used to determine Internet is available or unavailable via network connection type
 * */
data class NetworkStatus(
    val networkConnectionType: NetworkConnectionType = NetworkConnectionType.NONE
)


enum class NetworkConnectionType {
    WIFI,
    CELLULAR,
    NONE
}