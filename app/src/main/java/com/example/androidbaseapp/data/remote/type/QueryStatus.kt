package com.example.androidbaseapp.data.remote.type

enum class QueryStatus(val value: Int, val label: String) {
    CONFIRMED(value = 0, "Confirmed"),
    DEATHS(value = 1, "Deaths"),
    RECOVERED(value = 2, "Recovered"),
    ACTIVE(value = 3, "Active"),
}