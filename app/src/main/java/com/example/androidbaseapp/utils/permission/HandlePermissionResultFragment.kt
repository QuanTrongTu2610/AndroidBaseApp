package com.example.androidbaseapp.utils.permission

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.androidbaseapp.utils.Logger

/**
 * This class used to define a specific fragment handled permission
 * */
class HandlePermissionResultFragment : Fragment() {
    companion object {
        private const val PERMISSION_REQUEST_CODE = 1001
        const val TAG = "HandlePermissionResultFragment"
    }

    private val requestPermissions: HashMap<String, (Permission) -> Unit> = hashMapOf()

    private var onFragmentCreated: (() -> Unit)? = null

    // callback handle permission granted or not
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.forEach { (permission, isGranted) ->
                val permissionRequested = requestPermissions[permission]
                val permissionResult = if (isGranted) {
                    Permission(permission = permission, granted = true, oneTimeCall = false)
                } else {
                    Permission(
                        permission = permission,
                        granted = false,
                        oneTimeCall = !shouldShowRequestPermissionRationale(permission)
                    )
                }
                permissionRequested?.invoke(permissionResult)
            }
        }

    fun request(permission: Array<out String>) = if (isAdded) {
        requestPermissions(*permission)
    } else {
        onFragmentCreated = { requestPermissions(*permission) }
    }


    /**
     *  permissions will be requested util view is available
     * */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // it is deprecated and can be change to ViewModel, save state for ViewModel
        retainInstance = true
        onFragmentCreated?.invoke()
        onFragmentCreated = null
    }

    /**
     * request single or multiple permission
     * */
    private fun requestPermissions(vararg permissions: String?) {
        Logger.d("#requestPermissions")
        requestPermissionLauncher.launch(permissions)
    }

    /**
     * add permission to pool
     * */
    fun addPermissionRequest(permission: String, request: (Permission) -> Unit) {
        Logger.d("#addPermissionRequest $permission")
        requestPermissions[permission] = request
    }

    /**
     * get permission request from pool
     * */
    fun getPermissionRequest(permission: String): ((Permission) -> Unit)? {
        Logger.d("#getPermissionRequest $permission")
        return requestPermissions[permission]
    }

}