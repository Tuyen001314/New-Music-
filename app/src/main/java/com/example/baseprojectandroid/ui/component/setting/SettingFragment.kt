package com.example.baseprojectandroid.ui.component.setting

import android.util.Log
import android.widget.LinearLayout
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.databinding.FragmentSettingBinding
import com.example.baseprojectandroid.databinding.FragmentYourLibraryBinding
import com.example.baseprojectandroid.model.Music
import com.example.baseprojectandroid.model.Song
import com.example.baseprojectandroid.ui.base.BaseFragment
import com.example.baseprojectandroid.ui.base.BaseFragmentBinding
import com.example.baseprojectandroid.ui.component.library.adapter.YourLibraryAdapter
import com.example.baseprojectandroid.ui.component.library.adapter.YourLibraryPlaylistAdapter
import com.example.baseprojectandroid.ui.component.setting.adapter.SettingAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.util.logging.Logger


@AndroidEntryPoint
class SettingFragment: BaseFragmentBinding<FragmentSettingBinding, SettingViewModel>() {

    override fun getContentViewId(): Int = R.layout.fragment_setting
    private lateinit var adapter: SettingAdapter

    override fun initializeViews() {
        adapter = SettingAdapter(requireContext())
        var list = ArrayList<Music>()
        list.add(Music("bo"))
        list.add(Music("ung qua chung"))
        list.add(Music("meo meo meo meo"))
        list.add(Music("Tha thứ lỗi lầm"))
        list.add(Music("Lần cuối"))
        Log.d("buituyen", list.size.toString())

        adapter.submitList(list,true)

        dataBinding.recyclerViewUpload.adapter = adapter
    }

    override fun registerListeners() {
        super.registerListeners()

        dataBinding.titleYourLibrary.setOnClickListener {

        }
    }

    override fun initializeData() {
        super.initializeData()
    }

    override fun registerObservers() {
        super.registerObservers()
    }
}