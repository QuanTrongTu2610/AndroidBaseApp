package com.example.androidbaseapp.common.exceptions

import java.io.IOException

/**
 * Defined general Network IO exceptions
 * */
sealed class NetworkConnectionException : IOException() {
    data class NoConnectivityException(
        override val message: String = ""
    ) : NetworkConnectionException()

    data class TooManyRequestException(
        override val message: String = ""
    ): NetworkConnectionException()
}