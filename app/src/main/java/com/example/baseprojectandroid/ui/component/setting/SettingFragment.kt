package com.example.baseprojectandroid.ui.component.setting

import android.content.Intent
import android.util.Log
import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.databinding.FragmentSettingBinding
import com.example.baseprojectandroid.model.Music
import com.example.baseprojectandroid.ui.base.BaseFragmentBinding
import com.example.baseprojectandroid.ui.component.library.profile.ProfileFragment
import com.example.baseprojectandroid.ui.component.setting.adapter.SettingAdapter
import com.example.baseprojectandroid.ui.component.splash.SplashActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SettingFragment : BaseFragmentBinding<FragmentSettingBinding, SettingViewModel>() {

    override fun getContentViewId(): Int = R.layout.fragment_setting
    private lateinit var adapter: SettingAdapter

    override fun initializeViews() {
        adapter = SettingAdapter(requireContext())
        dataBinding.recyclerViewUpload.adapter = adapter
    }

    override fun registerListeners() {
        super.registerListeners()

        dataBinding.viewProfile.setOnClickListener {
            openFragment(ProfileFragment())
        }

        dataBinding.tvLogOut.setOnClickListener {
            viewModel.logout()
            startActivity(Intent(requireContext(), SplashActivity::class.java))
            requireActivity().finish()
        }
    }

    override fun initializeData() {
        super.initializeData()
    }

    override fun registerObservers() {
        super.registerObservers()
    }
}