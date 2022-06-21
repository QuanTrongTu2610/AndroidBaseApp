package com.example.androidbaseapp.data.repositories.model

data class WorldWipModel(
    val worldId: Int = 0,
    val newConfirmed: Int = 0,
    val totalConfirmed: Int = 0,
    val newDeaths: Int = 0,
    val newRecovered: Int = 0,
    val totalDeaths: Int = 0,
    val totalRecovered: Int = 0,
    val date: String = "",
)