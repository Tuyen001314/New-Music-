package com.example.baseprojectandroid.ui.component.library

import android.util.Log
import android.widget.LinearLayout
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.databinding.FragmentYourLibraryBinding
import com.example.baseprojectandroid.model.Music
import com.example.baseprojectandroid.model.Song
import com.example.baseprojectandroid.ui.base.BaseFragment
import com.example.baseprojectandroid.ui.base.BaseFragmentBinding
import com.example.baseprojectandroid.ui.component.library.adapter.YourLibraryAdapter
import com.example.baseprojectandroid.ui.component.library.adapter.YourLibraryPlaylistAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.util.logging.Logger


@AndroidEntryPoint
class YourLibraryFragment: BaseFragmentBinding<FragmentYourLibraryBinding, YourLibraryViewModel>() {

    override fun getContentViewId(): Int = R.layout.fragment_your_library
    private lateinit var adapter: YourLibraryAdapter
    private lateinit var adapterPlaylist: YourLibraryPlaylistAdapter

    override fun initializeViews() {
        adapter = YourLibraryAdapter(requireContext())
        adapterPlaylist = YourLibraryPlaylistAdapter(requireContext())
        var list = ArrayList<Music>()
        list.add(Music("bo"))
        list.add(Music("ung qua chung"))
        list.add(Music("meo meo meo meo"))
        list.add(Music("Tha thứ lỗi lầm"))
        list.add(Music("Lần cuối"))
        Log.d("buituyen", list.size.toString())
        adapter.submitList(list,true)
        adapterPlaylist.submitList(list, true)
        dataBinding.recyclerViewUpload.adapter = adapter
        //dataBinding.recyclerViewUpload.isNestedScrollingEnabled = false
        dataBinding.recyclerViewDownload.adapter = adapter
        //dataBinding.recyclerViewDownload.isNestedScrollingEnabled = false
        dataBinding.recyclerRecentPlay.adapter = adapter
        //dataBinding.recyclerRecentPlay.isNestedScrollingEnabled = false
        dataBinding.recyclerPlaylist.adapter = adapterPlaylist
        //dataBinding.recyclerPlaylist.isNestedScrollingEnabled = false
        dataBinding.recyclerPlaylist.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    override fun registerListeners() {
        super.registerListeners()
    }

    override fun initializeData() {
        super.initializeData()
    }

    override fun registerObservers() {
        super.registerObservers()
    }
}