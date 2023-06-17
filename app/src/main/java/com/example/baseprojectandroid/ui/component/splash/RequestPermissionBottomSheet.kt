package com.example.baseprojectandroid.ui.component.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.documentviewer.filereader.pro.databinding.BottomSheetRequestPermissionBinding
import com.documentviewer.filereader.pro.extension.visibleOrGone
import com.documentviewer.filereader.pro.utils.Logger
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class RequestPermissionBottomSheet(var isSplashScreen: Boolean = false) :
    BottomSheetDialogFragment() {
    private lateinit var dataBinding: BottomSheetRequestPermissionBinding
    private lateinit var clickConfirmYes: () -> Unit
    private lateinit var clickConfirmNo: () -> Unit

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            val ft: FragmentTransaction = manager.beginTransaction()
            ft.add(this, tag)
            ft.commitAllowingStateLoss()
        } catch (e: IllegalStateException) {
            Logger.e("File Reader", "Exception : $e")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = BottomSheetRequestPermissionBinding.inflate(inflater, container, false)
        dataBinding.imgRequest.visibleOrGone(isSplashScreen)
        dataBinding.txtContent.visibleOrGone(isSplashScreen)
        dataBinding.txtContent2.visibleOrGone(!isSplashScreen)

        dataBinding.btnConfirmYes.setOnClickListener {
            clickConfirmYes.invoke()
            dismiss()
        }
        dataBinding.btnCancel.setOnClickListener {
            clickConfirmNo.invoke()
            dismiss()
        }
        return dataBinding.root
    }

    //----------------------------------------------------------------------------------------------
    fun onClickConfirmYes(clickConfirmYes: () -> Unit) {
        this.clickConfirmYes = clickConfirmYes
    }

    fun onClickConfirmNo(clickConfirmNo: () -> Unit) {
        this.clickConfirmNo = clickConfirmNo
    }

}