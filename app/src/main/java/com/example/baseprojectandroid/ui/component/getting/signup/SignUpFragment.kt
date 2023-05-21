package com.example.baseprojectandroid.ui.component.getting.signup

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.databinding.FragmentGettingBinding
import com.example.baseprojectandroid.databinding.FragmentSignUpBinding
import com.example.baseprojectandroid.ui.base.BaseFragment
import com.example.baseprojectandroid.ui.base.BaseFragmentBinding
import com.example.baseprojectandroid.ui.component.MainActivity
import com.example.baseprojectandroid.ui.component.getting.GettingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment: BaseFragmentBinding<FragmentSignUpBinding, SignUpViewModel>()  {
    override fun getContentViewId(): Int = R.layout.fragment_sign_up

    override fun registerListeners() {
        super.registerListeners()
        dataBinding.btnNextSignUp.setOnClickListener {
            if (viewModel.getUser(dataBinding.textUserName.text.toString())) {
                val bundle = Bundle().apply {
                    putString("username", dataBinding.textUserName.text.toString())
                }
                findNavController().navigate(R.id.action_signUpFragment_to_passwordFragment)
            }
        }
    }
}