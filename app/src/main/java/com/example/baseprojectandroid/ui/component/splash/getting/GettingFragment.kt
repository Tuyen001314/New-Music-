package com.example.baseprojectandroid.ui.component.splash.getting

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.databinding.FragmentGettingBinding
import com.example.baseprojectandroid.ui.base.BaseFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GettingFragment : BaseFragmentBinding<FragmentGettingBinding, GettingViewModel>()   {
    override fun getContentViewId() = R.layout.fragment_getting

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initializeViews() {
    }

    override fun registerListeners() {
        dataBinding.buttonSignUp.setOnClickListener {
            goToSignUpFragment()
        }
        dataBinding.textView.setOnClickListener {
            goToSignInFragment()
        }
    }

    override fun initializeData() {

    }

    override fun registerObservers() {

    }

    private fun goToSignInFragment() {
        findNavController().navigate(R.id.action_gettingFragment_to_signInFragment)
    }

    private fun goToSignUpFragment() {
        findNavController().navigate(R.id.action_gettingFragment_to_signUpFragment)
    }
}