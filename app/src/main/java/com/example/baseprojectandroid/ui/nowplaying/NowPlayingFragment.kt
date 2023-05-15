package com.example.baseprojectandroid.ui.nowplaying

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.databinding.FragmentNowPlayingBinding
import com.example.baseprojectandroid.model.SongState
import com.example.baseprojectandroid.service.MusicService
import com.example.baseprojectandroid.ui.base.BaseFragmentBinding
import com.example.baseprojectandroid.ui.base.BaseViewModel
import com.example.baseprojectandroid.viewmodel.NowPlayingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class NowPlayingFragment : BaseFragmentBinding<FragmentNowPlayingBinding, BaseViewModel>() {
    private val nowPlayingViewModel by activityViewModels<NowPlayingViewModel>()

    override fun getContentViewId(): Int {
        return R.layout.fragment_now_playing
    }

    override fun initializeViews() {

    }

    override fun registerObservers() {
        super.registerObservers()
        nowPlayingViewModel.onServiceConnected = {
            lifecycleScope.launch(Dispatchers.Default) {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    nowPlayingViewModel.currentSongState?.collect {
                        withContext(Dispatchers.Main) {
                            when (it.state) {
                                SongState.STATE_PLAYING -> dataBinding.btPauseResume.setImageResource(
                                    R.drawable.ic_pause
                                )
                                SongState.STATE_PAUSE -> dataBinding.btPauseResume.setImageResource(
                                    R.drawable.ic_triangle
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    override fun registerListeners() {
        dataBinding.btPauseResume.setOnClickListener {
            nowPlayingViewModel.pauseOrPlay()
        }
    }

    override fun initializeData() {
    }
}

