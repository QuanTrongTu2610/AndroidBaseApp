package com.example.androidbaseapp.common.permission

/**
 * Permission model
 *
 * @param oneTimeCall ask user for only one time
 * */
data class Permission(
    val permission: String,
    val granted: Boolean = false,
    val oneTimeCall: Boolean = false
)
