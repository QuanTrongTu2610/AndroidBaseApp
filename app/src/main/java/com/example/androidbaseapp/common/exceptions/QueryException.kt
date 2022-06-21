package com.example.androidbaseapp.common.exceptions

import java.lang.Exception

/**
 * Defined errors related to query fetching api
 * */
sealed class QueryException : Exception() {
    class UndefinedQueryException(
        override val message: String? = ""
    ) : QueryException()
}