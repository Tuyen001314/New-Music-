package com.example.baseprojectandroid.ui.component.library.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.databinding.ItemPlaylistLibraryBinding
import com.example.baseprojectandroid.databinding.ItemYourLibraryBinding
import com.example.baseprojectandroid.model.Music
import com.example.baseprojectandroid.ui.base.SimpleAdapter

class YourLibraryPlaylistAdapter(context: Context) :
    SimpleAdapter<Music, YourLibraryPlaylistAdapter.ViewHolder, ItemPlaylistLibraryBinding>(context) {

    override fun getItemViewId(viewType: Int): Int = R.layout.item_playlist_library

    override fun getViewHolder(viewType: Int, dataBinding: ItemPlaylistLibraryBinding) =
        ViewHolder(dataBinding)

    override fun createViewBinding(parent: ViewGroup): ItemPlaylistLibraryBinding {
        return ItemPlaylistLibraryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): YourLibraryPlaylistAdapter.ViewHolder {
        dataBinding = ItemPlaylistLibraryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return super.onCreateViewHolder(parent, viewType)
    }

    //----------------------------------------------------------------------------------------------
    inner class ViewHolder(var dataBinding: ItemPlaylistLibraryBinding) :
        SimpleAdapter.ViewHolder(dataBinding) {
        init {

        }

        override fun bindViews(position: Int) {
            val item = getItem(position)
        }
    }

}