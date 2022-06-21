package com.example.androidbaseapp.common.permission

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.fragment.app.FragmentActivity
import com.example.androidbaseapp.common.Logger

class PermissionUtil constructor(private val owner: FragmentActivity) {

    private val requestFragment: HandlePermissionResultFragment

    // activity will add fragment to request permission
    init {
        Logger.d("PermissionUtils Initialization")
        val fragment = owner
            .supportFragmentManager
            .findFragmentByTag(HandlePermissionResultFragment.TAG)
        if (fragment == null) {
            requestFragment = HandlePermissionResultFragment()
            // start permission fragment
            owner
                .supportFragmentManager
                .beginTransaction()
                .add(requestFragment, HandlePermissionResultFragment.TAG)
                .commit()
        } else {
            requestFragment = fragment as HandlePermissionResultFragment
        }
        Logger.d("requestFragment = $requestFragment")
    }

    /**
     * handle request single or multiple permissions
     * */
    fun request(
        permissions: List<String>,
        requestPermissionResultListener: RequestPermissionResultListener
    ) {
        Logger.d("PermissionUtils Request Permission")
        permissions.forEach { Logger.d("request: $it") }

        val permissionSize = permissions.size
        val permissionsResult = arrayListOf<Permission>()
        val requestedPermissions = arrayListOf<String>()

        permissions.forEach { permission ->
            if (isGranted(permission)) {
                permissionsResult.add(
                    Permission(
                        permission = permission,
                        granted = true,
                        oneTimeCall = false
                    )
                )
            } else {
                // if permission is not grant -> should ask
                var subject = requestFragment.getPermissionRequest(permission)
                if (subject == null) {
                    subject = fun(permissionResult: Permission) {
                        permissionsResult.add(permissionResult)
                        if (permissionsResult.size == permissionSize) {
                            val isGrantedAll = permissionsResult.all { it.granted }
                            if (isGrantedAll) {
                                requestPermissionResultListener.onRequestedPermissionResults(
                                    areGrantedAll = isGrantedAll,
                                    grantedPermissions = permissionsResult,
                                    deniedPermissions = emptyList()
                                )
                            } else {
                                val permissionsNotGranted = permissionsResult.filter { !it.granted }
                                val permissionGranted = permissionsResult.filter { it.granted }
                                requestPermissionResultListener.onRequestedPermissionResults(
                                    areGrantedAll = isGrantedAll,
                                    grantedPermissions = permissionGranted,
                                    deniedPermissions = permissionsNotGranted
                                )
                            }
                        }
                    }
                }
                requestFragment.addPermissionRequest(permission, subject)
                requestedPermissions.add(permission)
            }
        }

        if (requestedPermissions.isNotEmpty() && !isPermissionLowerAPI6()) {
            requestFragment.request(requestedPermissions.toTypedArray())
        } else {
            requestPermissionResultListener.onRequestedPermissionResults(
                areGrantedAll = true,
                grantedPermissions = emptyList(),
                deniedPermissions = emptyList()
            )
        }
    }

    private fun isGranted(permission: String) = owner.isPermissionsGranted(permission)
}

/**
 * Extension to check permission is granted or not
 * */
fun Context.isPermissionsGranted(vararg permissions: String): Boolean {
    if (isPermissionLowerAPI6()) return true

    permissions.forEach { permission ->
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            return false
        }
    }
    return true
}

/**
 * Almost permission lower than api 6 are granted before asking user when install application
 * */
private fun isPermissionLowerAPI6(): Boolean = Build.VERSION.SDK_INT < Build.VERSION_CODES.M
