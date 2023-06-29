package com.example.baseprojectandroid.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.example.baseprojectandroid.R

object AlertDialogUtil {
    internal enum class DialogType {
        LOADING,
        MESSAGE
    }

    //    private var isLoadingDialogShowing = false
    private var isMessageDialogShowing = false

    // ---------------------------------------------------------------------------------------------
    private fun createDialog(
        context: Context,
        cancelable: Boolean,
        view: View,
        transparentWindow: Boolean = false,
        dialogType: DialogType = DialogType.MESSAGE
    ): AlertDialog {
        val dialog = AlertDialog.Builder(context)
            .setCancelable(cancelable)
            .setView(view)
            .create()

        dialog.window?.decorView?.setBackgroundResource(android.R.color.transparent)
        if (transparentWindow) {
            dialog.window?.setDimAmount(0F)
        }

        dialog.setOnDismissListener {
            if (dialogType == DialogType.MESSAGE) {
                isMessageDialogShowing = false
            }
        }

        dialog.setOnCancelListener {
            if (dialogType == DialogType.MESSAGE) {
                isMessageDialogShowing = false
            }
        }

        return dialog
    }

    private fun isMessageDialogShowing(): Boolean {
        if (isMessageDialogShowing) {
            return true
        }
        isMessageDialogShowing = true
        return false
    }

    @SuppressLint("InflateParams")
    fun showLoadingDialog(context: Context): AlertDialog {

        val view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null)
//        val imgLoading = view.findViewById(R.id.imgLoading) as ImageView

        val dialog = createDialog(context, false, view, true, DialogType.LOADING)
        dialog.show()
        return dialog
    }

}