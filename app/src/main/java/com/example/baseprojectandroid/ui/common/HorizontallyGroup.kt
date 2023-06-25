package com.example.baseprojectandroid.ui.common

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.databinding.ItemCarouselBinding
import com.example.baseprojectandroid.extension.dp
import com.xwray.groupie.Group
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.viewbinding.BindableItem

class HorizontallyGroup(
    private val offsetItem: Int = 8.dp
) : BindableItem<ItemCarouselBinding>() {
    private val adapter = GroupieAdapter()

    override fun bind(viewBinding: ItemCarouselBinding, position: Int) {
        viewBinding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
            adapter = this@HorizontallyGroup.adapter
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    outRect.right = offsetItem
                }
            })
        }
    }

    override fun getLayout(): Int {
        return R.layout.item_carousel
    }

    override fun initializeViewBinding(view: View): ItemCarouselBinding {
        return ItemCarouselBinding.bind(view)
    }

    fun add(group: Group) {
        adapter.add(group)
    }

    fun addAll(groups: List<Group>) {
        adapter.addAll(groups)
    }

    fun update(groups: List<Group>) {
        adapter.update(groups)
    }
}