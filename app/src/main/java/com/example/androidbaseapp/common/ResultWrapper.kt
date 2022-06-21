package com.example.androidbaseapp.common

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class ResultWrapper<out T : Any> {

    data class Success<out T : Any>(val data: T) : ResultWrapper<T>()
    data class Error(val exception: Exception) : ResultWrapper<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}
