package com.example.baseprojectandroid.ui.component.splash.getting.signin

import android.content.Intent
import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.databinding.FragmentSignInBinding
import com.example.baseprojectandroid.ui.base.BaseFragmentBinding
import com.example.baseprojectandroid.ui.component.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : BaseFragmentBinding<FragmentSignInBinding, SignInViewModel>() {
    override fun getContentViewId(): Int = R.layout.fragment_sign_in

    override fun registerListeners() {
        dataBinding.btnNextSignIn.setOnClickListener {
            viewModel.signIn(
                dataBinding.textUserName.text.toString(),
                dataBinding.textUserPass.text.toString()
            )
        }
    }

    override fun registerObservers() {
        super.registerObservers()
        viewModel.signInResponseLiveData.observe(viewLifecycleOwner) {
            it?.let {
                it.whenSuccess {
                    startActivity(Intent(context, MainActivity::class.java))
                    requireActivity().finish()
                }.whenFailure {
                    showToast("Failure. Message = ${it.message}")
                }.whenLoading {
                    showToast("Loading")
                }
            }
        }
    }

    override fun onBackPressed(): Boolean {
        return false
    }

}