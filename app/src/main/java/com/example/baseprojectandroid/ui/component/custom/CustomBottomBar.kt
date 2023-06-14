package com.example.baseprojectandroid.ui.component.custom

import android.content.Context
import android.util.AttributeSet
import androidx.core.view.children
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CustomBottomBar : BottomNavigationView {


    private var viewPager: ViewPager2? = null
    private var currentPosition = 0

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    )

    constructor(
        context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        setOnItemSelectedListener { item ->
            val position = menu.children.indexOf(item)
            if (currentPosition != position) {
                viewPager?.setCurrentItem(position, false)
                currentPosition = position
            }
            true
        }
    }

    fun setWithViewPager(viewPager: ViewPager2) {
        if (this.viewPager == null) {
            this.viewPager = viewPager
            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    currentPosition = position
                    menu.getItem(position).isChecked = true
                }
            })
        }
    }
}