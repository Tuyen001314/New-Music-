package com.example.baseprojectandroid.ui.component.splash.getting.signup

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.databinding.FragmentSignUpBinding
import com.example.baseprojectandroid.model.AccountState
import com.example.baseprojectandroid.ui.base.BaseFragmentBinding
import com.example.baseprojectandroid.ui.base.launchSafe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class SignUpFragment : BaseFragmentBinding<FragmentSignUpBinding, SignUpViewModel>() {
    override fun getContentViewId(): Int = R.layout.fragment_sign_up

    override fun registerListeners() {
        dataBinding.btnNextSignUp.setOnClickListener {
            if (dataBinding.textUserName.text.length >= 8) {
                checkInfoUserFromServer(dataBinding.textUserName.text.toString())
            }
            else {
                showToast("ten dang nhap tu 8 ky tu tro len")
            }
        }
    }

    private fun checkInfoUserFromServer(userName: String) {
        viewModel.viewModelScope.launchSafe(Dispatchers.IO) {
            viewModel.fetchUser(userName).collect { state ->
                when (state) {
                    is AccountState.Loading -> {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "loading....", Toast.LENGTH_SHORT).show()
                        }
                    }

                    is AccountState.Finished -> {
                        val bundle = Bundle().apply {
                            putString("username", dataBinding.textUserName.text.toString().trim())
                        }
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "success", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.action_signUpFragment_to_passwordFragment, bundle)
                        }
                    }

                    is AccountState.Failed -> {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "account exist", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}