package com.example.baseprojectandroid.ui.component.splash.getting.signup.name

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.databinding.FragmentNameBinding
import com.example.baseprojectandroid.local.LocalStorage
import com.example.baseprojectandroid.model.AccountState
import com.example.baseprojectandroid.ui.base.BaseFragmentBinding
import com.example.baseprojectandroid.ui.component.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NameFragment : BaseFragmentBinding<FragmentNameBinding, NameViewModel>() {

    @Inject
    lateinit var localStorage: LocalStorage

    override fun getContentViewId(): Int  = R.layout.fragment_name

    override fun initializeData() {
        viewModel.initData(requireArguments())
    }

    override fun registerListeners() {
        dataBinding.btnCreateAccount.setOnClickListener {
            //callApiRegister()
            if (dataBinding.edtNameUser.text.isNotEmpty()) {
                saveDataUser(viewModel.username, viewModel.password, dataBinding.edtNameUser.text.toString())
                startActivity(Intent(context, MainActivity::class.java))
            }
        }
    }

    private fun saveDataUser(username: String, password: String, nameUser: String) {
        localStorage.username = username
        localStorage.password = password
        localStorage.nameUser = nameUser
        localStorage.isFirstTime = false
    }

    private fun callApiRegister() {
        viewModel.viewModelScope.launch(Dispatchers.IO) {
            viewModel.callApiRegister().collect { state ->
                when (state) {
                    is AccountState.Loading -> {
                        showToast("Đang chờ xử lý. Vui lòng đợi trong giây lát")
                    }
                    is AccountState.Finished -> {
                        showToast("Đăng ký thành công")
                        startActivity(Intent(context, MainActivity::class.java))
                    }
                    is AccountState.Failed -> {
                        showToast("Đăng ký thất bại")
                    }
                }
            }
        }
    }
}