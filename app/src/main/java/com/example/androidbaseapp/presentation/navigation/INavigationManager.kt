package com.example.androidbaseapp.presentation.navigation

import androidx.navigation.NavDirections

interface INavigationManager {

    fun navigate(navDirections: NavDirections)

    fun setOnNavEvent(navEventListener: (navDirections: NavDirections) -> Unit)
}