package com.example.androidbaseapp.presentation

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.androidbaseapp.common.permission.RequestPermissionResultListener
import com.example.androidbaseapp.common.views.SafetyClickListener

interface IBaseFragment : DialogCommonView {

    /**
     * Base activity @see [BaseActivity] which this fragment are standing on.
     * */
    val baseActivity: BaseActivity?


    /**
     * Handle Click event between views to prevent fast click
     * */
    val safetyClick: SafetyClickListener


    /**
     * Method to get layout xml id
     * @return a layout xml id.
     */
    fun getLayoutId(): Int

    /**
     * Receive data from which screen open this screen.
     * @param data [Bundle]: Composite received data.
     */
    fun initData(data: Bundle?)

    /**
     * Init views in screen
     */
    fun initViews()

    /**
     * Declare listener on views
     */
    fun initActions()

    /**
     * Declare observers
     */
    fun initObservers()

    /**
     * Show loading view.
     */
    fun showLoading()

    /**
     * Hide loading view.
     */
    fun hideLoading()


    /**
     * Invoke [baseActivity] to request permissions
     * */
    fun requestPermissions(
        permissions: List<String>,
        listener: RequestPermissionResultListener
    ) {
        baseActivity?.requestPermissions(
            permissions = permissions,
            listener = listener
        )
    }

    /**
     * Show Double options dialog
     * */
    override fun showDoubleOptionsDialog(
        title: String?,
        message: String,
        firstButton: String,
        secondButton: String,
        firstButtonListener: DialogButtonClickListener?,
        secondButtonListener: DialogButtonClickListener?,
        isCancellable: Boolean
    ): AlertDialog? {
        return baseActivity?.showDoubleOptionsDialog(
            title = title,
            message = message,
            firstButton = firstButton,
            secondButton = secondButton,
            firstButtonListener = firstButtonListener,
            secondButtonListener = secondButtonListener,
            isCancellable = isCancellable
        )
    }

}