package com.example.baseprojectandroid.ui.component.library

import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.databinding.FragmentAddPlaylistBinding
import com.example.baseprojectandroid.ui.base.BaseFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddAPlaylistFragment :
    BaseFragmentBinding<FragmentAddPlaylistBinding, AddAPlaylistViewModel>() {
    override fun getContentViewId(): Int = R.layout.fragment_add_playlist

}