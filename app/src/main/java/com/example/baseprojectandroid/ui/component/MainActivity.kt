package com.example.baseprojectandroid.ui.component

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.databinding.ActivityMainBinding
import com.example.baseprojectandroid.service.MusicServiceConnector
import com.example.baseprojectandroid.ui.PlaylistSampleFragment
import com.example.baseprojectandroid.ui.base.BaseActivityBinding
import com.example.baseprojectandroid.ui.base.BaseViewModel
import com.example.baseprojectandroid.ui.nowplaying.NowPlayingFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivityBinding<ActivityMainBinding, BaseViewModel>() {
    override fun onViewCreated(savedInstanceState: Bundle?) {
        super.onViewCreated(savedInstanceState)
        supportFragmentManager.commit {
            add(dataBinding.nowPlayingContainer.id, NowPlayingFragment(), null)
        }
    }

    override fun getContentViewId(): Int {
        return R.layout.activity_main
    }
}