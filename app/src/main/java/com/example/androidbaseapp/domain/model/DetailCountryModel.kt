package com.example.androidbaseapp.domain.model

data class DetailCountryModel(
    val id: String = "",
    val countryId: Int = 0,
    val active: Int = 0,
    val city: String = "",
    val cityCode: String = "",
    val confirmed: Int = 0,
    val country: String = "",
    val countryCode: String = "",
    val date: String = "",
    val deaths: Int = 0,
    val lat: String = "",
    val lon: String = "",
    val province: String = "",
    val recovered: Int = 0
)