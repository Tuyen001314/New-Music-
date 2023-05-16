package com.example.baseprojectandroid.ui.component

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.service.MusicServiceConnector
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var musicServiceConnector: MusicServiceConnector
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        musicServiceConnector.bindToService(this)
    }
}