package com.example.baseprojectandroid.ui.component.getting

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.databinding.FragmentGettingBinding
import com.example.baseprojectandroid.databinding.FragmentPlaylistSampleBinding
import com.example.baseprojectandroid.ui.base.BaseFragment
import com.example.baseprojectandroid.ui.base.BaseFragmentBinding
import com.example.baseprojectandroid.ui.base.BaseViewModel
import com.example.baseprojectandroid.ui.component.MainActivity
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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