package com.example.baseprojectandroid.ui.common

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.databinding.LayoutMusicItemBinding
import com.example.baseprojectandroid.model.SongState
import com.xwray.groupie.Item
import com.xwray.groupie.viewbinding.BindableItem

class SongStateItem(
    val songState: SongState
) : BindableItem<LayoutMusicItemBinding>() {
    override fun bind(viewBinding: LayoutMusicItemBinding, position: Int) {
        viewBinding.tvSongName.text = songState.song.name
        if (songState.state == SongState.STATE_PLAYING) {
            viewBinding.tvSongName.setTextColor(Color.parseColor("#1ED760"))
        } else {
            viewBinding.tvSongName.setTextColor(Color.BLACK)
        }
        viewBinding.ivThumb.apply {
            Glide.with(this)
                .load(songState.song.thumbnailUrl)
                .override(100, 100)
                .transform(CenterCrop(), RoundedCorners(20))
                .dontAnimate()
                .into(object : SimpleTarget<Drawable>() {
                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable>?
                    ) {
                        viewBinding.ivThumb.setImageDrawable(resource)
                    }

                })
        }
    }

    override fun getLayout(): Int {
        return R.layout.layout_music_item
    }

    override fun initializeViewBinding(view: View): LayoutMusicItemBinding {
        return LayoutMusicItemBinding.bind(view)
    }

    override fun isSameAs(other: Item<*>): Boolean {
        other as SongStateItem
        return songState.song.url == other.songState.song.url
    }

    override fun hasSameContentAs(other: Item<*>): Boolean {
        other as SongStateItem
        return songState.state == other.songState.state
    }

}