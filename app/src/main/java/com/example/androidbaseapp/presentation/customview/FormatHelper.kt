package com.example.androidbaseapp.presentation.customview

import java.text.NumberFormat
import java.util.*

object FormatHelper {
    fun convertToStatisticFormat(
        number: Float,
        postFix: String = "",
        preFix: String = ""
    ): String {
        val locale = Locale("en", "US")
        val currencyFormatter = NumberFormat.getCurrencyInstance(locale)
        return "$preFix${currencyFormatter.format(number)
            .let { it.substring(1, it.length - 3).replace(",", ".") }}$postFix"
    }
}