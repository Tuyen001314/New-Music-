package com.example.baseprojectandroid.extension

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.example.baseprojectandroid.utils.AlertDialogUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private var loadingDialog: AlertDialog? = null
fun FragmentActivity.showLoadingDialog() {
    if (loadingDialog != null) {
        loadingDialog!!.dismiss()
        loadingDialog = null
    }

    loadingDialog = AlertDialogUtil.showLoadingDialog(this)
    loadingDialog?.show()
}

fun FragmentActivity.hideLoadingDialog() {
    loadingDialog?.dismiss()
}

fun FragmentActivity.showToast(context: Context, message: String) {
    CoroutineScope(Dispatchers.Main).launch {
        Toast.makeText(
            context,
            message,
            Toast.LENGTH_SHORT
        )
            .show()
    }
}