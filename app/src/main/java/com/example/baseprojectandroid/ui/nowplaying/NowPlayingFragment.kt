package com.example.baseprojectandroid.ui.nowplaying

import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class NowPlayingFragment : BaseFragmentBinding<FragmentNowPlayingBinding, BaseViewModel>(), MotionLayout.TransitionListener {
    private val nowPlayingViewModel by activityViewModels<NowPlayingViewModel>()

    override fun getContentViewId(): Int {
        return R.layout.fragment_now_playing
    }

    override fun initializeViews() {

    }

    override fun registerObservers() {
        super.registerObservers()
        lifecycleScope.launch(Dispatchers.Default) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                nowPlayingViewModel.uiState.collect { uiState ->
                    uiState.songState.getValueIfNotHandle(viewLifecycleOwner) { songState ->
                        updateNowPlayingSong(songState)
                    }

                    updatePosition(uiState.currentPosition)
                }
            }
        }
    }

    private fun updateNowPlayingSong(songState: SongState) {
        lifecycleScope.launch(Dispatchers.Main) {
            dataBinding.tvState.text = songState.song.name
        }
    }

    private fun updatePosition(position: Position) {
        lifecycleScope.launch(Dispatchers.Main) {
            dataBinding.songProgress.progress =
                ((position.currentIndex.toFloat() / position.duration) * 100).toInt()
        }
    }

    override fun registerListeners() {
        dataBinding.btPauseResume.setOnClickListener {
            nowPlayingViewModel.pauseOrPlay()
        }
    }

    override fun initializeData() {
    }

    override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {

    }

    override fun onTransitionChange(
        motionLayout: MotionLayout?,
        startId: Int,
        endId: Int,
        progress: Float
    ) {
//        requireView().updateLayoutParams {
//            height = (1000* (1 - progress)).toInt() + 100
//        }
    }

    override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
    }

    override fun onTransitionTrigger(
        motionLayout: MotionLayout?,
        triggerId: Int,
        positive: Boolean,
        progress: Float
    ) {
    }
}

