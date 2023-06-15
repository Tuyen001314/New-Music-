package com.example.baseprojectandroid.ui.component.home

import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.databinding.FragmentHomeBinding
import com.example.baseprojectandroid.ui.base.BaseFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragmentBinding<FragmentHomeBinding, HomeViewModel>() {
    override fun getContentViewId(): Int = R.layout.fragment_home

}