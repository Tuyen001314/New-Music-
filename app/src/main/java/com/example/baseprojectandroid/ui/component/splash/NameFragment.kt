package com.example.baseprojectandroid.ui.component.splash

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.ui.component.MainActivity

class NameFragment : Fragment(R.layout.fragment_name) {

    lateinit var btnCreateAccount: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnCreateAccount = view.findViewById(R.id.btnCreateAccount)

        btnCreateAccount.setOnClickListener {
            startActivity(Intent(context, MainActivity::class.java))
        }
    }
}