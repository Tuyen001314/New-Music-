package com.example.baseprojectandroid.ui.component.library.upload

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.databinding.FragmentUploadDetailBinding
import com.example.baseprojectandroid.extension.gone
import com.example.baseprojectandroid.extension.visible
import com.example.baseprojectandroid.ui.base.BaseFragmentBinding
import com.example.baseprojectandroid.ui.base.BaseViewModel
import com.example.baseprojectandroid.ui.component.library.UploadTrackViewModel

class UploadTrackDetailFragment :
    BaseFragmentBinding<FragmentUploadDetailBinding, BaseViewModel>() {
    private val uploadTrackViewModel by activityViewModels<UploadTrackViewModel>()

    override fun getContentViewId(): Int {
        return R.layout.fragment_upload_detail
    }

    override fun registerObservers() {
        super.registerObservers()
        uploadTrackViewModel.uploadSongResponse.observe(viewLifecycleOwner) {
            it.whenFailure {
                dataBinding.apply {
                    groupFailure.visible()
                    groupSuccess.gone()
                    groupProcessing.gone()
                    dataBinding.tvFailureMessage.text = it.message
                }
            }.whenSuccess {
                dataBinding.apply {
                    groupFailure.gone()
                    groupSuccess.visible()
                    groupProcessing.gone()
                }
            }.whenLoading {
                dataBinding.apply {
                    groupFailure.gone()
                    groupSuccess.gone()
                    groupProcessing.visible()
                }
            }
        }
    }


    override fun registerListeners() {
        super.registerListeners()
        dataBinding.btBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed(): Boolean {
        removeFragment(this)
        return true
    }

}