package com.example.baseprojectandroid.ui.nowplaying

import android.view.View
import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.databinding.LayoutViewPagerNowPlayingLiteBinding
import com.example.baseprojectandroid.model.Song
import com.xwray.groupie.viewbinding.BindableItem

class SongInNowPlayingViewPagerItem(
    private val song: Song
) : BindableItem<LayoutViewPagerNowPlayingLiteBinding>() {
    override fun bind(viewBinding: LayoutViewPagerNowPlayingLiteBinding, position: Int) {
        viewBinding.tvSongName.text = song.name
        viewBinding.tvCreator.text = song.creator.name
    }

    override fun getLayout(): Int = R.layout.layout_view_pager_now_playing_lite

    override fun initializeViewBinding(view: View): LayoutViewPagerNowPlayingLiteBinding {
        return LayoutViewPagerNowPlayingLiteBinding.bind(view)
    }
}