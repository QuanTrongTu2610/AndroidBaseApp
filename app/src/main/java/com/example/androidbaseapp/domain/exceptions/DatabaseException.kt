package com.example.androidbaseapp.domain.exceptions

/**
 * Defined general Database IO exceptions
 * */
sealed class DatabaseException : Exception() {
    data class NotFoundOrNullEntity(
        override val message: String = ""
    ) : DatabaseException()
}