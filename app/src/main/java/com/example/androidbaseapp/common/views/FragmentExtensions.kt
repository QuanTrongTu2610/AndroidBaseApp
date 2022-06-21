package com.example.androidbaseapp.common.views

import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.androidbaseapp.R
import com.example.androidbaseapp.common.Logger

/**
 * Navigate to specified fragment
 * */
fun Fragment.navigateSafe(direction: NavDirections, navOptions: NavOptions? = null) {
    if (this.canNavigate()) findNavController().navigate(direction, navOptions)
}

/**
 * This function used to determine current fragment is currently available or not
 * */
fun Fragment.canNavigate(): Boolean {

    val destinationIdInNav =
        view?.findNavController()?.currentDestination?.id

    val destinationIdInFragmentContainer =
        view?.getTag(R.id.tag_navigation_destination_id) ?: destinationIdInNav

    return if (destinationIdInNav == destinationIdInFragmentContainer) {
        view?.setTag(R.id.tag_navigation_destination_id, destinationIdInFragmentContainer)
        true
    } else {
        Logger.d("Can not navigate")
        false
    }
}