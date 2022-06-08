package com.example.androidbaseapp.presentation.base

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.androidbaseapp.utils.permission.PermissionUtil
import com.example.androidbaseapp.utils.permission.RequestPermissionResultListener
import dagger.hilt.android.AndroidEntryPoint

/**
 * BaseActivity used to defined general implementations for all activities
 * */
@AndroidEntryPoint
abstract class BaseActivity : AppCompatActivity(), DialogCommonView {

    private var permissionUtil: PermissionUtil? = null

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(getLayoutId())
        permissionUtil = PermissionUtil(this)
    }

    /**
     * Method to get layout xml id (e.g., R.layout.screen_abc)
     * @return a layout xml id.
     */
    abstract fun getLayoutId(): Int

    /**
     * Request permissions
     * */
    fun requestPermissions(
        permissions: List<String>,
        listener: RequestPermissionResultListener
    ) {
        permissionUtil?.request(
            permissions = permissions,
            requestPermissionResultListener = listener
        )
    }

    override fun showDoubleOptionsDialog(
        title: String?,
        message: String,
        firstButton: String,
        secondButton: String,
        firstButtonListener: DialogButtonClickListener?,
        secondButtonListener: DialogButtonClickListener?,
        isCancellable: Boolean
    ): AlertDialog? {
        return buildDialog(title, message)
            .setPositiveButton(firstButton) { dialog, _ ->
                firstButtonListener?.invoke(dialog) ?: dialog.dismiss()
            }
            .setNegativeButton(secondButton) { dialog, _ ->
                secondButtonListener?.invoke(dialog) ?: dialog.dismiss()
            }
            .setCancelable(isCancellable)
            .show()
    }

    private fun buildDialog(title: String?, message: String): AlertDialog.Builder {
        return AlertDialog.Builder(this)
            .setCancelable(false)
            .setTitle(title)
            .setMessage(message)
    }
}