package com.example.baseprojectandroid.ui.component.library.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.databinding.ItemYourLibraryBinding
import com.example.baseprojectandroid.model.Music
import com.example.baseprojectandroid.model.SongEntity
import com.example.baseprojectandroid.ui.base.SimpleAdapter
import java.io.File

class YourLibraryAdapter(context: Context) :
    SimpleAdapter<SongEntity, YourLibraryAdapter.ViewHolder, ItemYourLibraryBinding>(context) {

    override fun getItemViewId(viewType: Int): Int = R.layout.item_your_library

    override fun getViewHolder(viewType: Int, dataBinding: ItemYourLibraryBinding) =
        ViewHolder(dataBinding)

    override fun createViewBinding(parent: ViewGroup): ItemYourLibraryBinding {
        return ItemYourLibraryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): YourLibraryAdapter.ViewHolder {
        dataBinding = ItemYourLibraryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return super.onCreateViewHolder(parent, viewType)
    }

    //----------------------------------------------------------------------------------------------
    inner class ViewHolder(var dataBinding: ItemYourLibraryBinding) :
        SimpleAdapter.ViewHolder(dataBinding) {
        init {

        }

        @SuppressLint("CheckResult")
        override fun bindViews(position: Int) {
            val item = getItem(position)
            dataBinding.nameSong.text = item.name
            Glide.with(dataBinding.root)
                .asBitmap()
                .load(item.thumbnailUrl)
                .error(R.drawable.ic_thumbnail_default)
                .into(dataBinding.appCompatImageView)
            dataBinding.author.text = item.creatorName
        }
    }

}