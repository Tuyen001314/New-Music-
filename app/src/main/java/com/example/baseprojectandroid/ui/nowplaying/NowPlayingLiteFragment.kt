package com.example.baseprojectandroid.ui.nowplaying

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.databinding.NowPlayingLiteFragmentBinding
import com.example.baseprojectandroid.model.SongState
import com.example.baseprojectandroid.ui.base.BaseFragmentBinding
import com.example.baseprojectandroid.ui.base.BaseViewModel
import com.example.baseprojectandroid.viewmodel.NowPlayingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NowPlayingLiteFragment: BaseFragmentBinding<NowPlayingLiteFragmentBinding, BaseViewModel>() {
    private val nowPlayingViewModel by activityViewModels<NowPlayingViewModel>()
    override fun getContentViewId(): Int {
        return R.layout.now_playing_lite_fragment
    }

    override fun registerObservers() {
        super.registerObservers()
        lifecycleScope.launch(Dispatchers.Default) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                nowPlayingViewModel.uiState.collect { uiState ->
                    uiState.songState.getValueIfNotHandle(viewLifecycleOwner) { songState ->
                        updateNowPlayingSong(songState)
                    }
                }
            }
        }
    }

    private fun updateNowPlayingSong(songState: SongState) {
        lifecycleScope.launch(Dispatchers.Main) {
            dataBinding.tvSongName.text = songState.song.name
            dataBinding.btPauseResume.setImageResource(
                when(songState.state) {
                    SongState.STATE_PLAYING -> R.drawable.ic_pause
                    else -> R.drawable.ic_triangle
                }
            )
            dataBinding.ivThumb.apply {
                Glide.with(this)
                    .load(songState.song.thumbnailUrl)
                    .override(100, 100)
                    .transform(CenterCrop(), RoundedCorners(20))
                    .into(this)
            }
        }
    }

    override fun registerListeners() {
        super.registerListeners()
        dataBinding.btPauseResume.setOnClickListener {
            nowPlayingViewModel.pauseOrPlay()
        }
    }
}