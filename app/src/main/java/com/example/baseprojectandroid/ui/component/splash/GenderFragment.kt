package com.example.baseprojectandroid.ui.component.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.baseprojectandroid.R

class GenderFragment: Fragment(R.layout.fragment_gender) {

    lateinit var btnNext: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnNext = view.findViewById(R.id.btnNext)

        btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_genderFragment_to_nameFragment)
        }
    }
}