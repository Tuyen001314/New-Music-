package com.example.baseprojectandroid.ui.component.search

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.databinding.ItemYourLibraryBinding
import com.example.baseprojectandroid.databinding.LayoutCommonHorizontalItemBinding
import com.example.baseprojectandroid.model.Music
import com.example.baseprojectandroid.model.Song
import com.example.baseprojectandroid.ui.base.SimpleAdapter

class SearchAdapter(context: Context) :
    SimpleAdapter<Song, SearchAdapter.ViewHolder, LayoutCommonHorizontalItemBinding>(context) {

    override fun getItemViewId(viewType: Int): Int = R.layout.layout_common_horizontal_item

    override fun getViewHolder(viewType: Int, dataBinding: LayoutCommonHorizontalItemBinding) =
        ViewHolder(dataBinding)

    override fun createViewBinding(parent: ViewGroup): LayoutCommonHorizontalItemBinding {
        return LayoutCommonHorizontalItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchAdapter.ViewHolder {
        dataBinding = LayoutCommonHorizontalItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return super.onCreateViewHolder(parent, viewType)
    }

    //----------------------------------------------------------------------------------------------
    inner class ViewHolder(var dataBinding: LayoutCommonHorizontalItemBinding) :
        SimpleAdapter.ViewHolder(dataBinding) {
        init {

        }

        override fun bindViews(position: Int) {
            val item = getItem(position)

        }
    }

}