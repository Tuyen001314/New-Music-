package com.example.baseprojectandroid.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.baseprojectandroid.databinding.BottomSheetCreatePlaylistBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CreatePlaylistBottomSheet : BottomSheetDialogFragment() {
    private lateinit var dataBinding: BottomSheetCreatePlaylistBinding
    private lateinit var clickConfirmDelete: () -> Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = BottomSheetCreatePlaylistBinding.inflate(inflater, container, false)
        return dataBinding.root
    }

    //----------------------------------------------------------------------------------------------
    fun onClickConfirmDelete(clickConfirmDelete: () -> Unit) {
        this.clickConfirmDelete = clickConfirmDelete
    }



}