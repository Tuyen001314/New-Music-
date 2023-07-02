package com.example.baseprojectandroid.ui.component.option

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.databinding.FragmentTrackOptionBinding
import com.example.baseprojectandroid.model.Song
import com.example.baseprojectandroid.ui.base.BaseFragmentBinding
import com.example.baseprojectandroid.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TrackOptionFragment :
    BaseFragmentBinding<FragmentTrackOptionBinding, BaseViewModel>() {
    private val trackOptionViewModel by viewModels<TrackOptionViewModel>()
    private lateinit var song: Song
    override fun getContentViewId(): Int {
        return R.layout.fragment_track_option
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        song = requireArguments().getParcelable("song")!!
        trackOptionViewModel.setCurrentSong(song)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun initializeViews() {
        super.initializeViews()

        //load anh
        Glide.with(this)
            .asBitmap()
            .load(song.thumbnailUrl)
            .error(R.drawable.ic_thumbnail_default)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    dataBinding.ivTrackThumb.setImageBitmap(resource)
                    //tao gradient cho background
                    Palette.from(resource).generate {
                        it?.getVibrantColor(Color.parseColor("#86929A"))?.let { color ->
                            lifecycleScope.launch(Dispatchers.Main) {
                                val gradient = GradientDrawable(
                                    GradientDrawable.Orientation.TOP_BOTTOM,
                                    intArrayOf(
                                        color,
                                        ColorUtils.blendARGB(color, Color.BLACK, 0.4f),
                                        ColorUtils.blendARGB(color, Color.BLACK, 0.8f),
                                        ColorUtils.blendARGB(color, Color.BLACK, 0.9f),
                                        Color.BLACK,
                                        Color.BLACK
                                    )
                                )
                                dataBinding.root.background = gradient
                            }
                        }
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }

            })

        dataBinding.tvSongNameMain.text = song.name
        dataBinding.tvSongCreatorName.text = song.name
    }

    override fun onBackPressed(): Boolean {
        parentFragmentManager.commit {
            remove(this@TrackOptionFragment)
        }
        return true
    }

    override fun registerListeners() {
        super.registerListeners()
        dataBinding.btLiked.setOnClickListener {
            showToast("Coming")
        }
        dataBinding.btDownload.setOnClickListener {
            showToast("Coming")
        }
        dataBinding.btAddToPlaylist.setOnClickListener {
            showToast("Coming")
        }
        dataBinding.btShare.setOnClickListener {
            showToast("Coming")
        }
    }

    companion object {
        fun newInstance(song: Song): TrackOptionFragment {
            val args = Bundle()
            args.putParcelable("song", song)
            val fragment = TrackOptionFragment()
            fragment.arguments = args
            return fragment
        }
    }
}