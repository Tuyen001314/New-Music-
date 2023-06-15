package com.example.baseprojectandroid.ui.common

import android.util.Size
import android.view.View
import com.bumptech.glide.Glide
import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.databinding.LayoutCommonSongVerticalItemBinding
import com.example.baseprojectandroid.extension.dp
import com.example.baseprojectandroid.model.Playlist
import com.example.baseprojectandroid.model.Song
import com.xwray.groupie.Item
import com.xwray.groupie.viewbinding.BindableItem
import com.xwray.groupie.viewbinding.GroupieViewHolder

class CommonSongVerticalItem(
    private val song: Song? = null,
    private val playlist: Playlist? = null,
    private val modifier: Modifier = Modifier.new(),
    private val onClick: (Any) -> Unit
): BindableItem<LayoutCommonSongVerticalItemBinding>() {
    override fun bind(viewBinding: LayoutCommonSongVerticalItemBinding, position: Int) {
        viewBinding.apply {
            viewBinding.tvTitle.text = song?.name ?: playlist?.name

            Glide.with(viewBinding.root)
                .load(song?.thumbnailUrl ?: playlist?.songs?.getOrNull(0))
                .override(modifier.imageSize.width, modifier.imageSize.height)
                .apply {
                    if (modifier.imageShape == ImageShape.CIRCLE) circleCrop()
                }
                .into(viewBinding.ivThumb)

            viewBinding.root.setOnClickListener {
                onClick(song ?: playlist!!)
            }
        }
    }

    override fun isSameAs(other: Item<*>): Boolean {
        return if (other is CommonSongVerticalItem) {
            song?.id == other.song?.id || playlist?.id == other.playlist?.id
        } else super.isSameAs(other)
    }

    override fun hasSameContentAs(other: Item<*>): Boolean {
        return if (other is CommonSongVerticalItem) {
            song == other.song || playlist?.id == other.playlist?.id
        } else super.hasSameContentAs(other)
    }

    override fun getLayout(): Int {
        return R.layout.layout_common_song_vertical_item
    }

    override fun initializeViewBinding(view: View): LayoutCommonSongVerticalItemBinding {
        return LayoutCommonSongVerticalItemBinding.bind(view)
    }

    data class Modifier(
        var imageSize: Size = Size(50.dp, 50.dp),
        var imageShape: ImageShape = ImageShape.CIRCLE
    ) {
        fun size(size: Size) = apply {
            imageSize = size
        }

        fun shape(shape: ImageShape) = apply {
            imageShape = shape
        }

        companion object {
            fun new() = Modifier()
        }
    }

    enum class ImageShape {
        RECT,
        CIRCLE
    }

    enum class ItemType {
        SONG,
        PLAYLIST
    }
}