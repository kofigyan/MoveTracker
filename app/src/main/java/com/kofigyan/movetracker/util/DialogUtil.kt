package com.kofigyan.movetracker.util

import android.content.Context
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.kofigyan.movetracker.R

object DialogUtil {

    var dialog: AlertDialog? = null

    private fun showAlertDialog(context: Context, dialogBuilder: AlertDialog.Builder.() -> Unit) {
        if (isShowing()) return
        val builder = AlertDialog.Builder(context)
        builder.dialogBuilder()
        dialog = builder.create()

        dialog?.show()

    }

    private fun isShowing() = dialog?.isShowing ?: false

    private fun AlertDialog.Builder.positiveButton(
        text: String = "Ok",
        handleClick: (which: Int) -> Unit = {}
    ) {
        this.setPositiveButton(text) { _, which -> handleClick(which) }
    }

    private fun AlertDialog.Builder.negativeButton(
        text: String = "Cancel",
        handleClick: (which: Int) -> Unit = {}
    ) {
        this.setNegativeButton(text) { _, which -> handleClick(which) }
    }


    fun showSingleGenericAlertDialog(
        context: Context, @StringRes title: Int,
        @StringRes message: Int,
        cancelable: Boolean = true, @StringRes positiveText: Int = R.string.dialog_postive_ok,
        action: (which: Int) -> Unit = {}
    ) {
        showAlertDialog(context) {
            setTitle(title)
            setMessage(message)
            setCancelable(cancelable)
            positiveButton(context.getString(positiveText)) {
                action(it)
            }
        }
    }

    fun showAllGenericAlertDialog(
        context: Context, @StringRes title: Int,
        @StringRes message: Int,
        cancelable: Boolean = true,
        @StringRes positiveText: Int = R.string.dialog_postive_ok,
        @StringRes negativeText: Int = R.string.dialog_postive_cancel,
        action: (which: Int) -> Unit = {}
    ) {
        showAlertDialog(context) {
            setTitle(title)
            setMessage(message)
            setCancelable(cancelable)
            positiveButton(context.getString(positiveText)) {
                action(it)
            }
            negativeButton(context.getString(negativeText)) { }
        }
    }


}