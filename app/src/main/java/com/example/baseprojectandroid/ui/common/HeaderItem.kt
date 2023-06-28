package com.example.baseprojectandroid.ui.common

import android.view.View
import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.databinding.LayoutCommonHeaderItemBinding
import com.xwray.groupie.viewbinding.BindableItem

class HeaderItem(
    private val headerName: String
) : BindableItem<LayoutCommonHeaderItemBinding>() {
    override fun bind(viewBinding: LayoutCommonHeaderItemBinding, position: Int) {
        viewBinding.tvHeader.text = headerName
    }

    override fun getLayout(): Int {
        return R.layout.layout_common_header_item
    }

    override fun initializeViewBinding(view: View): LayoutCommonHeaderItemBinding {
        return LayoutCommonHeaderItemBinding.bind(view)

    }
}