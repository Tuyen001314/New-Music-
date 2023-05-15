package com.example.baseprojectandroid.ui.common

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.databinding.LayoutMusicItemBinding
import com.example.baseprojectandroid.model.SongState
import com.xwray.groupie.viewbinding.BindableItem

class SongStateItem(
    val songState: SongState
): BindableItem<LayoutMusicItemBinding>() {
    override fun bind(viewBinding: LayoutMusicItemBinding, position: Int) {
        viewBinding.tvSongName.text = songState.song.name
        viewBinding.ivThumb.apply {
            Glide.with(this)
                .load(songState.song.thumbnailUrl)
                .override(100, 100)
                .transform(CenterCrop(), RoundedCorners(20))
                .into(this)
        }
    }

    override fun getLayout(): Int {
        return R.layout.layout_music_item
    }

    override fun initializeViewBinding(view: View): LayoutMusicItemBinding {
        return LayoutMusicItemBinding.bind(view)
    }
}