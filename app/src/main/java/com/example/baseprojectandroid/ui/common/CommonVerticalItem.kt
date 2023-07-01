package com.example.baseprojectandroid.ui.common

import android.graphics.Color
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.databinding.LayoutCommonVerticalItemBinding
import com.example.baseprojectandroid.ui.SongUiState
import com.xwray.groupie.Item
import com.xwray.groupie.viewbinding.BindableItem
import java.util.Objects

class CommonVerticalItem(
    private val songUiState: SongUiState,
    private val onClick: () -> Unit
) : BindableItem<LayoutCommonVerticalItemBinding>() {
    override fun bind(viewBinding: LayoutCommonVerticalItemBinding, position: Int) {
        Glide.with(viewBinding.root.context)
            .load(songUiState.song.thumbnailUrl)
            .override(100, 100)
            .transform(CenterCrop(), RoundedCorners(20))
            .into(viewBinding.ivThumb)

        viewBinding.tvCreator.text = songUiState.song.creator.name
        viewBinding.tvSongName.text = songUiState.song.name
        viewBinding.tvSongName.setTextColor(
            if (songUiState.isPlaying) Color.parseColor("#1ED760") else Color.WHITE
        )
        viewBinding.root.setOnClickListener {
            onClick()
        }
    }

    override fun hasSameContentAs(other: Item<*>): Boolean {
        if (other is CommonVerticalItem) {
            return Objects.deepEquals(this, other)
        }
        return super.hasSameContentAs(other)
    }

    override fun isSameAs(other: Item<*>): Boolean {
        if (other is CommonVerticalItem) {
            return songUiState.song.id == other.songUiState.song.id
        }
        return super.isSameAs(other)
    }

    override fun getLayout(): Int {
        return R.layout.layout_common_vertical_item
    }

    override fun initializeViewBinding(view: View): LayoutCommonVerticalItemBinding {
        return LayoutCommonVerticalItemBinding.bind(view)
    }
}