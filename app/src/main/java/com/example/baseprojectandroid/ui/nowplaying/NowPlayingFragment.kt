package com.example.baseprojectandroid.ui.nowplaying

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Handler
import android.os.Looper
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.core.animation.doOnEnd
import androidx.core.graphics.ColorUtils
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.media3.common.Player
import com.bumptech.glide.Glide
import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.databinding.FragmentNowPlayingBinding
import com.example.baseprojectandroid.model.Position
import com.example.baseprojectandroid.model.Song
import com.example.baseprojectandroid.service.PlayerState
import com.example.baseprojectandroid.ui.base.BaseFragmentBinding
import com.example.baseprojectandroid.ui.base.BaseViewModel
import com.example.baseprojectandroid.ui.component.option.TrackOptionFragment
import com.example.baseprojectandroid.ui.event.HideDetailPlayer
import com.example.baseprojectandroid.utils.Logger
import com.example.baseprojectandroid.utils.timePositionToString
import com.example.baseprojectandroid.viewmodel.NowPlayingUiEffect
import com.example.baseprojectandroid.viewmodel.NowPlayingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import java.util.concurrent.atomic.AtomicBoolean

@AndroidEntryPoint
class NowPlayingFragment : BaseFragmentBinding<FragmentNowPlayingBinding, BaseViewModel>() {
    private val nowPlayingViewModel by activityViewModels<NowPlayingViewModel>()
    private var isSeekbarTracking = AtomicBoolean(false)
    private var currentMainColorBackground = Color.BLACK

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
                        uiState.song.getValueIfNotHandle(viewLifecycleOwner) { songState ->
                            updateNowPlayingSong(songState)
                        }

                        uiState.currentState.getValueIfNotHandle(viewLifecycleOwner) { playerState ->
                            updatePlayerState(playerState)
                        }

                        updatePosition(uiState.currentPosition)
                    }
                }

                launch {
                    nowPlayingViewModel.thumbVibrantColor.collect {
                        changeSmoothBackground(it)
                    }
                }

                launch {
                    nowPlayingViewModel.uiEffect.collect {
                        it.getValueIfNotHandle { uiEffect ->
                            when (uiEffect) {
                                is NowPlayingUiEffect.ShowTrackOption -> {
                                    requireActivity().supportFragmentManager.commit {
                                        add(
                                            android.R.id.content,
                                            TrackOptionFragment.newInstance(uiEffect.song),
                                            "track_option"
                                        )
                                    }
                                }
                                else -> {}
                            }
                        }
                    }
                }
            }
        }
    }

    private fun updatePlayerState(playerState: PlayerState) {
        lifecycleScope.launch(Dispatchers.Main) {
            dataBinding.apply {
                dataBinding.btPauseResume.setImageResource(
                    if (playerState.isPlaying) R.drawable.ic_pause else R.drawable.ic_triangle
                )
                ImageViewCompat.setImageTintList(
                    btShuffle,
                    if (playerState.isShuffle) ColorStateList.valueOf(Color.parseColor("#1DB954"))
                    else null
                )
                btRepeat.setImageResource(
                    when (playerState.repeatMode) {
                        Player.REPEAT_MODE_OFF -> R.drawable.ic_repeat
                        Player.REPEAT_MODE_ALL -> R.drawable.ic_repeat_all
                        else -> R.drawable.ic_repeat_one
                    }
                )
            }
        }
    }

    private fun changeSmoothBackground(targetColor: Int) {
        lifecycleScope.launch(Dispatchers.Main) {
            val animation = ValueAnimator.ofObject(
                ArgbEvaluator(),
                currentMainColorBackground,
                targetColor
            )
            animation
                .setDuration(250)
                .apply {
                    addUpdateListener {
                        val color = it.animatedValue as Int
                        lifecycleScope.launch(Dispatchers.Main) {
                            val gradient = GradientDrawable(
                                GradientDrawable.Orientation.TOP_BOTTOM,
                                intArrayOf(color, ColorUtils.blendARGB(color, Color.BLACK, 0.3f))
                            )
                            dataBinding.root.background = gradient
                        }
                    }
                    doOnEnd {
                        currentMainColorBackground = targetColor
                    }
                }
                .start()
        }
    }

    private fun updateNowPlayingSong(song: Song) {
        lifecycleScope.launch(Dispatchers.Main) {
            dataBinding.apply {
                tvSongName.text = song.name
                tvSongNameMain.text = song.name
                tvSongCreatorName.text = song.creator.name
                Logger.d("trackthumb = ${song.thumbnailUrl}")
                Glide.with(ivTrackThumb)
                    .asBitmap()
                    .load(song.thumbnailUrl)
                    .error(R.drawable.ic_thumbnail_default)
                    .into(ivTrackThumb)
            }
        }
    }

    private fun updatePosition(position: Position) {
        lifecycleScope.launch(Dispatchers.Main) {
            dataBinding.tvTrackCurrentPosition.text = position.currentIndex.timePositionToString()
            if (!isSeekbarTracking.get()) {
                dataBinding.songProgress.progress =
                    ((position.currentIndex.toFloat() / position.duration) * 100).toInt()
                dataBinding.tvTrackTotalTime.text = position.duration.timePositionToString()
            }
        }
    }

    override fun registerListeners() {
        dataBinding.btCollap.setOnClickListener {
            EventBus.getDefault().post(HideDetailPlayer())
        }

        dataBinding.btMore.setOnClickListener {
            nowPlayingViewModel.onClickShowTrackOption()
        }

        dataBinding.btPauseResume.setOnClickListener {
            nowPlayingViewModel.pauseOrPlay()
        }

        dataBinding.btNext.setOnClickListener {
            nowPlayingViewModel.nextSong()
        }

        dataBinding.btPrev.setOnClickListener {
            nowPlayingViewModel.prevSong()
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

        dataBinding.btShuffle.setOnClickListener {
            nowPlayingViewModel.onClickShuffle()
        }

        dataBinding.btRepeat.setOnClickListener {
            nowPlayingViewModel.onClickRepeat()
        }
    }

    override fun onBackPressed(): Boolean {
        EventBus.getDefault().post(HideDetailPlayer())
        return true
    }

    override fun initializeData() {

    }

    companion object {
        const val TAG = "NowPlayingFragment"
    }
}

