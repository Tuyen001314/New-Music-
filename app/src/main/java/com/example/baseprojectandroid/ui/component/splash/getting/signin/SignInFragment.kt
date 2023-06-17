package com.example.baseprojectandroid.ui.component.splash.getting.signin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.databinding.FragmentSignInBinding
import com.example.baseprojectandroid.databinding.FragmentSignUpBinding
import com.example.baseprojectandroid.model.AccountState
import com.example.baseprojectandroid.ui.base.BaseFragment
import com.example.baseprojectandroid.ui.base.BaseFragmentBinding
import com.example.baseprojectandroid.ui.component.MainActivity
import com.example.baseprojectandroid.ui.component.splash.getting.signup.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class SignInFragment : BaseFragmentBinding<FragmentSignInBinding, SignInViewModel>() {
    override fun getContentViewId(): Int = R.layout.fragment_sign_in

    override fun registerListeners() {
        dataBinding.btnNextSignIn.setOnClickListener {
            checkInfoUserFromServer(dataBinding.textUserName.text.toString(), dataBinding.textUserPass.text.toString())
            startActivity(Intent(context, MainActivity::class.java))
            requireActivity().finish()
        }
    }

    override fun onBackPressed(): Boolean {
        return false
    }

     private fun checkInfoUserFromServer(username: String, password: String) {
        viewModel.viewModelScope.launch(Dispatchers.IO) {
            /*viewModel.fetchUser(username).collect { state ->
                when (state) {
                    is AccountState.Loading -> {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "loading....", Toast.LENGTH_SHORT).show()
                        }
                    }

                    is AccountState.Finished -> {
                        val bundle = Bundle().apply {
                            putString("username", dataBinding.textUserName.text.toString())
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
            }*/
        }
    }
}