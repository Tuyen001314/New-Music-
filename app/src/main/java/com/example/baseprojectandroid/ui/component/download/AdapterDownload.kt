package com.example.baseprojectandroid.ui.component.download

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.databinding.ItemYourLibraryBinding
import com.example.baseprojectandroid.model.Music
import com.example.baseprojectandroid.ui.base.SimpleAdapter

class DownloadAdapter(context: Context) :
    SimpleAdapter<Music, DownloadAdapter.ViewHolder, ItemYourLibraryBinding>(context) {

    val listData = mutableListOf<Music>()

    private var onClick: ((View, Int, Music) -> Unit)? = null

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
    ): DownloadAdapter.ViewHolder {
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

        override fun bindViews(position: Int) {
            val item = getItem(position)
            dataBinding.nameSong.text = item.name
            dataBinding.appCompatImageView.setOnClickListener {
                invokeOnItemClicked(position)
            }
        }
    }

}