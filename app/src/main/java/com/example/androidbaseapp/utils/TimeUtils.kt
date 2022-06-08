package com.example.androidbaseapp.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*
import kotlin.Comparator

/**
 * This object used to define some helper function for date/time in project
 * */
object TimeUtils {

    private const val apiDateTimeFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    private const val defaultDateTime = "yyyy-MM-dd"

    fun getSpecialCurrentTime(minusDay: Int): String {
        val date = SimpleDateFormat(apiDateTimeFormat, Locale.getDefault())
        return date
            .format(Calendar.getInstance().apply { add(Calendar.DATE, -minusDay) }.time)
    }

    @SuppressLint("SimpleDateFormat")
    fun convertFromApiDateTimeToDefaultDateTime(apiDateTime: String): String {
        val previousDateFormat = SimpleDateFormat(apiDateTimeFormat)
        val orientedDateFormat = SimpleDateFormat(defaultDateTime)
        return orientedDateFormat.format(previousDateFormat.parse(apiDateTime))
    }

    @SuppressLint("SimpleDateFormat")
    fun convertDateToAdequateFormat(dateTime: String): String {
        val previousDateFormat = SimpleDateFormat(defaultDateTime)
        val orientedDateFormat = SimpleDateFormat(apiDateTimeFormat)
        return orientedDateFormat.format(previousDateFormat.parse(dateTime))
    }
}