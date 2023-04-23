package com.example.baseprojectandroid.ui.base

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import java.lang.reflect.ParameterizedType

abstract class BaseFragmentBinding<T : ViewDataBinding, V : BaseViewModel> : BaseFragment() {

    open lateinit var dataBinding: T
    open lateinit var viewModel: V

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        try {
            dataBinding = DataBindingUtil.bind(view)!!
            dataBinding.lifecycleOwner = this
            @Suppress("UNCHECKED_CAST")
            val clazz: Class<V> =
                (this.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<V>
            viewModel = ViewModelProvider(this)[clazz]
        } catch (e: Exception) {
            Log.d("buituyen", e.message.toString())
            return
        }
        if (isInitialized) {
            super.onViewCreated(view, savedInstanceState)
        }
    }

    protected val isInitialized get() = this::viewModel.isInitialized && this::dataBinding.isInitialized
}