package com.example.baseprojectandroid.ui.component.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.baseprojectandroid.databinding.BottomSheetRequestPermissionBinding
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
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = BottomSheetRequestPermissionBinding.inflate(inflater, container, false)
        dataBinding.imgRequest.isVisible = isSplashScreen
        dataBinding.txtContent.isVisible = isSplashScreen
        dataBinding.txtContent2.isGone = isSplashScreen

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