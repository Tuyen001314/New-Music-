package com.example.baseprojectandroid.ui.component.splash.getting.signup.pass

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.databinding.FragmentPasswordBinding
import com.example.baseprojectandroid.ui.base.BaseFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PasswordFragment : BaseFragmentBinding<FragmentPasswordBinding, PasswordViewModel>() {

    override fun getContentViewId(): Int = R.layout.fragment_password

    override fun initializeViews() {
        viewModel.initData(requireArguments())
    }

    override fun registerListeners() {
        dataBinding.btnNextPass.setOnClickListener {
            if (dataBinding.edtPass.text.length < 8) {
                showToast("pass phải có từ 8 ký tự trở lên")
            } else {
                val bundle = Bundle().apply {
                    putString(
                        "password",
                        dataBinding.edtPass.text.toString()
                    )
                    putString(
                        "username",
                        viewModel.userName
                    )
                }
                showToast("success")
                findNavController().navigate(R.id.action_passwordFragment_to_nameFragment, bundle)
            }
        }
    }

    override fun initializeData() {
        viewModel.initData(requireArguments())
    }

    override fun registerObservers() {

    }
}