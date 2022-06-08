package com.example.androidbaseapp.domain.interactor.types

enum class WorldWipQueryStatus(val value: String) {
    NEW_CONFIRMED(value = "NewConfirmed"),
    TOTAL_CONFIRMED(value = "TotalConfirmed"),
    NEW_DEATHS(value = "NewDeaths"),
    TOTAL_DEATHS(value = "TotalDeaths"),
    NEW_RECOVERED(value = "NewRecovered"),
    TOTAL_RECOVERED(value = "TotalRecovered")
}