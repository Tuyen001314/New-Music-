package com.example.baseprojectandroid.ui.component.search

import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment: BaseFragment() {
    override fun getContentViewId(): Int = R.layout.fragment_search
}