package com.example.baseprojectandroid.ui.component.setting.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.databinding.ItemSettingBinding
import com.example.baseprojectandroid.databinding.ItemYourLibraryBinding
import com.example.baseprojectandroid.model.Music
import com.example.baseprojectandroid.ui.base.SimpleAdapter

class SettingAdapter(context: Context) :
    SimpleAdapter<Music, SettingAdapter.ViewHolder, ItemSettingBinding>(context) {

    override fun getItemViewId(viewType: Int): Int = R.layout.item_setting

    override fun getViewHolder(viewType: Int, dataBinding: ItemSettingBinding) =
        ViewHolder(dataBinding)

    override fun createViewBinding(parent: ViewGroup): ItemSettingBinding {
        return ItemSettingBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SettingAdapter.ViewHolder {
        dataBinding = ItemSettingBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return super.onCreateViewHolder(parent, viewType)
    }

    //----------------------------------------------------------------------------------------------
    inner class ViewHolder(var dataBinding: ItemSettingBinding) :
        SimpleAdapter.ViewHolder(dataBinding) {
        init {

        }

        override fun bindViews(position: Int) {
            val item = getItem(position)
            dataBinding.nameSong.text = item.name
        }
    }

}