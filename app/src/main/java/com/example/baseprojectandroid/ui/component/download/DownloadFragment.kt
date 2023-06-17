package com.example.baseprojectandroid.ui.component.download

import android.util.Log
import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.databinding.FragmentDownloadBinding
import com.example.baseprojectandroid.model.Music
import com.example.baseprojectandroid.ui.base.BaseFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DownloadFragment : BaseFragmentBinding<FragmentDownloadBinding, DownloadViewModel>() {

    override fun getContentViewId() = R.layout.fragment_download

    private lateinit var adapter: DownloadAdapter

    override fun initializeViews() {
        adapter = DownloadAdapter(requireContext())

        var list = ArrayList<Music>()
        list.add(Music("bo"))
        list.add(Music("ung qua chung"))
        list.add(Music("meo meo meo meo"))
        list.add(Music("Tha thứ lỗi lầm"))
        list.add(Music("Lần cuối"))
        Log.d("buituyen", list.size.toString())
        adapter.submitList(list,true)

        dataBinding.recyclerView.adapter = adapter

    }

    override fun registerListeners() {

    }

    override fun initializeData() {

    }

    override fun registerObservers() {

    }
}