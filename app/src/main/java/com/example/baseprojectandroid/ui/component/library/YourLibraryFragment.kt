package com.example.baseprojectandroid.ui.component.library

import android.util.Log
import android.widget.LinearLayout
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.commit
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.databinding.FragmentGettingBinding
import com.example.baseprojectandroid.databinding.FragmentYourLibraryBinding
import com.example.baseprojectandroid.dialog.CreatePlaylistBottomSheet
import com.example.baseprojectandroid.model.Music
import com.example.baseprojectandroid.model.Song
import com.example.baseprojectandroid.ui.base.BaseFragment
import com.example.baseprojectandroid.ui.base.BaseFragmentBinding
import com.example.baseprojectandroid.ui.component.MainActivity
import com.example.baseprojectandroid.ui.component.download.DownloadFragment
import com.example.baseprojectandroid.ui.component.library.adapter.YourLibraryAdapter
import com.example.baseprojectandroid.ui.component.library.adapter.YourLibraryPlaylistAdapter
import com.example.baseprojectandroid.ui.component.setting.SettingFragment
import com.example.baseprojectandroid.ui.component.setting.adapter.SettingAdapter
import com.example.baseprojectandroid.ui.nowplaying.NowPlayingFragment
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
        dataBinding.recyclerViewDownload.adapter = adapter
        dataBinding.recyclerRecentPlay.adapter = adapter
        dataBinding.recyclerPlaylist.adapter = adapterPlaylist
        dataBinding.recyclerPlaylist.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    override fun registerListeners() {
        super.registerListeners()

        dataBinding.add.setOnClickListener {
            val createList = CreatePlaylistBottomSheet()
            createList.show(childFragmentManager, "")
            createList.onClickPlaylist {
                openFragment(AddAPlaylistFragment())
            }
            createList.onClickPublishSong {
                openFragment(UploadTrackFragment())
            }
        }

        dataBinding.seeAllDown.setOnClickListener {
            openFragment(DownloadFragment())
        }

        dataBinding.seeAllPlaylist.setOnClickListener {

        }

        dataBinding.seeAllRecent.setOnClickListener {

        }

        dataBinding.seeAllUpload.setOnClickListener {

        }

        dataBinding.titleYourLibrary.setOnClickListener {
            openFragment(SettingFragment())
        }

        dataBinding.avatarUser.setOnClickListener {
            openFragment(SettingFragment())
        }
    }

    override fun initializeData() {
        super.initializeData()
    }

    override fun registerObservers() {
        super.registerObservers()
    }
}