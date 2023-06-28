package com.example.baseprojectandroid.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.baseprojectandroid.databinding.BottomSheetCreatePlaylistBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CreatePlaylistBottomSheet : BottomSheetDialogFragment() {
    private lateinit var dataBinding: BottomSheetCreatePlaylistBinding
    private lateinit var onClickPlaylist: () -> Unit
    private lateinit var onClickPublishSong: () -> Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = BottomSheetCreatePlaylistBinding.inflate(inflater, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.playlist.setOnClickListener {
            onClickPlaylist.invoke()
            dismiss()
        }

        dataBinding.song.setOnClickListener {
            onClickPublishSong.invoke()
            dismiss()
        }
    }

    //----------------------------------------------------------------------------------------------
    fun onClickPlaylist(onClickPlaylist: () -> Unit) {
        this.onClickPlaylist = onClickPlaylist
    }

    fun onClickPublishSong(onClickPublishSong: () -> Unit) {
        this.onClickPublishSong = onClickPublishSong
    }




}