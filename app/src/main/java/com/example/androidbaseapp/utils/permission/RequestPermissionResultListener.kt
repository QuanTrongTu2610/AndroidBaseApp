package com.example.androidbaseapp.utils.permission

/**
 * This interface used to handle the result of requested permission
 * */
interface RequestPermissionResultListener {

    /**
     * @param areGrantedAll boolean
     * @param grantedPermissions list of permission
     * @param deniedPermissions list of permission
     * */
    fun onRequestedPermissionResults(
        areGrantedAll: Boolean,
        grantedPermissions: List<Permission>,
        deniedPermissions: List<Permission>
    )

}