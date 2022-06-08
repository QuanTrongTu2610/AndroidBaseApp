package com.example.androidbaseapp.presentation.base

import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog

/**
 * The interface define method to work with dialog.
 */
interface DialogCommonView {

    /**
     * Show dialog with two option buttons, usually for alert decide purpose.
     * The dialog is persist with back press, and when user click on one in two buttons, then the dialog will be
     * closed.
     *
     * @param title [String]: Text for title of dialog. It may be null when you don't want to show title.
     * @param message [String]: Text for message of dialog. It must not null, and we don't handle case empty.
     * @param firstButton [String]: Text for positive button. It must not null, and we don't handle case empty.
     * @param secondButton [String]: Text for negative button. It must not null, and we don't handle case empty.
     * @param firstButtonListener [DialogButtonClickListener]: It will be called when use click on positive button.
     * @param secondButtonListener [DialogButtonClickListener]: It will be called when use click on negative button.
     * @param isCancellable [Boolean]: is button is dismiss when no need to click buttons
     */
    fun showDoubleOptionsDialog(
        title: String? = null,
        message: String,
        firstButton: String,
        secondButton: String,
        firstButtonListener: DialogButtonClickListener? = null,
        secondButtonListener: DialogButtonClickListener? = null,
        isCancellable: Boolean = true
    ): AlertDialog?

}


typealias DialogButtonClickListener = (dialog: DialogInterface) -> Unit