package com.example.baseprojectandroid.ui.nowplaying

import android.content.res.ColorStateList
import android.os.Handler
import android.os.Looper
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.databinding.FragmentNowPlayingBinding
import com.example.baseprojectandroid.model.Position
import com.example.baseprojectandroid.model.SongState
import com.example.baseprojectandroid.ui.base.BaseFragmentBinding
import com.example.baseprojectandroid.ui.base.BaseViewModel
import com.example.baseprojectandroid.viewmodel.NowPlayingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

@AndroidEntryPoint
class NowPlayingFragment : BaseFragmentBinding<FragmentNowPlayingBinding, BaseViewModel>() {
    private val nowPlayingViewModel by activityViewModels<NowPlayingViewModel>()
    private var isSeekbarTracking = AtomicBoolean(false)

    private var wordHandler = Handler(Looper.getMainLooper())

    override fun getContentViewId(): Int {
        return R.layout.fragment_now_playing
    }

    override fun initializeViews() {

    }

    override fun registerObservers() {
        super.registerObservers()
        lifecycleScope.launch(Dispatchers.Default) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    nowPlayingViewModel.uiState.collect { uiState ->
                        uiState.songState.getValueIfNotHandle(viewLifecycleOwner) { songState ->
                            updateNowPlayingSong(songState)
                        }

                        updatePosition(uiState.currentPosition)
                    }
                }

                launch {
                    nowPlayingViewModel.thumbVibrantColor.collect {
                        dataBinding.root.backgroundTintList = ColorStateList.valueOf(it)
                    }
                }
            }
        }
    }

    private fun updateNowPlayingSong(songState: SongState) {
        lifecycleScope.launch(Dispatchers.Main) {
            dataBinding.apply {
                tvSongName.text = songState.song.name
                tvSongNameMain.text = songState.song.name
                tvSongCreatorName.text = songState.song.creator.name
                Glide.with(ivTrackThumb)
                    .load(songState.song.thumbnailUrl)
                    .into(ivTrackThumb)
            }
        }
    }

    private fun updatePosition(position: Position) {
        lifecycleScope.launch(Dispatchers.Main) {
            if (!isSeekbarTracking.get()) {
                dataBinding.songProgress.progress =
                    ((position.currentIndex.toFloat() / position.duration) * 100).toInt()
            }
        }
    }

    override fun registerListeners() {
        dataBinding.btPauseResume.setOnClickListener {
            nowPlayingViewModel.pauseOrPlay()
        }

        dataBinding.songProgress.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                isSeekbarTracking.set(true)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                //prevent update position instantly, when player hasn't sought to right process yet.
                wordHandler.postDelayed({
                    isSeekbarTracking.set(false)
                }, 1000L)
                nowPlayingViewModel.updatePosition(dataBinding.songProgress.progress)
            }
        })
    }

    override fun initializeData() {
    }

}

