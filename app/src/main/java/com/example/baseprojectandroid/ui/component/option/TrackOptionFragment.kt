package com.example.baseprojectandroid.ui.component.option

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
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
import com.example.baseprojectandroid.extension.downloadFile
import com.example.baseprojectandroid.extension.getDirFile
import com.example.baseprojectandroid.extension.gone
import com.example.baseprojectandroid.extension.visible
import com.example.baseprojectandroid.model.DownloadState
import com.example.baseprojectandroid.model.Song
import com.example.baseprojectandroid.model.SongEntity
import com.example.baseprojectandroid.ui.base.BaseFragmentBinding
import com.example.baseprojectandroid.ui.base.BaseViewModel
import com.example.baseprojectandroid.ui.component.splash.RequestPermissionBottomSheet
import com.example.baseprojectandroid.utils.Constants
import com.example.baseprojectandroid.utils.MusicUtils
import com.example.baseprojectandroid.utils.MusicUtils.shareMusic
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@AndroidEntryPoint
class TrackOptionFragment : BaseFragmentBinding<FragmentTrackOptionBinding, BaseViewModel>() {
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
        Glide.with(this).asBitmap().load(song.thumbnailUrl).diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    dataBinding.ivTrackThumb.setImageBitmap(resource)
                    //tao gradient cho background
                    Palette.from(resource).generate {
                        it?.getVibrantColor(Color.parseColor("#86929A"))?.let { color ->
                            lifecycleScope.launch(Dispatchers.Main) {
                                val gradient = GradientDrawable(
                                    GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(
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


        val path = getDirFile().path + "/" + song.name
        lifecycleScope.launch(Dispatchers.IO) {
            if (checkMusicDownFromRoom(path)) {
                withContext(Dispatchers.Main) {
                    dataBinding.ivDownload.visible()
                    dataBinding.animationView.gone()
                    dataBinding.ivDownload.setImageDrawable(resources.getDrawable(R.drawable.downloaded))
                }
            }

            if (checkFavoriteFromRoom(song.id.toString())) {
                withContext(Dispatchers.Main) {
                    dataBinding.ivLike.background = resources.getDrawable(R.drawable.favorited)
                }
            }
        }
    }

    private fun checkFavoriteFromRoom(id: String) = trackOptionViewModel.getFavorite(id) != null

    private fun checkMusicDownFromRoom(path: String) = trackOptionViewModel.get(path) != null

    override fun onBackPressed(): Boolean {
        parentFragmentManager.commit {
            remove(this@TrackOptionFragment)
        }
        return true
    }

    override fun registerListeners() {
        super.registerListeners()
        dataBinding.btLiked.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                if (checkFavoriteFromRoom(song.id.toString())) {
                    trackOptionViewModel.deleteFavorite(song.id.toString())
                    withContext(Dispatchers.Main) {
                        dataBinding.ivLike.background = resources.getDrawable(R.drawable.ic_like_stroke)
                    }
                } else {
                    trackOptionViewModel.handleFavorite(song)
                    withContext(Dispatchers.Main) {
                        dataBinding.ivLike.background = resources.getDrawable(R.drawable.favorited)
                    }
                }
            }
        }
        dataBinding.btDownload.setOnClickListener {
            handleClickButtonDownload(song.url, song.name)
        }
        dataBinding.btAddToPlaylist.setOnClickListener {
            showToast("Coming")
        }
        dataBinding.btShare.setOnClickListener {
            val path = getDirFile().path + "/" + song.name
            try {
                val uriForFile: Uri = if (Build.VERSION.SDK_INT >= 24) {
                    FileProvider.getUriForFile(
                        requireActivity().applicationContext,
                        "${requireActivity().packageName}.provider",
                        File(path)
                    )
                } else {
                    Uri.fromFile(File(path))
                }
                shareMusic(requireActivity(), uriForFile)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Can't share this music", Toast.LENGTH_LONG).show()
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun handleClickButtonDownload(url: String, name: String) {
        if (!MusicUtils.checkPermission(requireContext())) {
            requestPermission()
            return
        }
        Toast.makeText(
            context, "doing download, please wait time", Toast.LENGTH_LONG
        ).show()
        dataBinding.animationView.visible()
        dataBinding.ivDownload.gone()
        CoroutineScope(Dispatchers.IO).launch {
            val flow = context?.let {
                downloadFile(
                    url, it, "$name.mp3"
                )
            }
            if (flow != null) {
                flow.collect { state ->
                    when (state) {
                        is DownloadState.Downloading -> {
                            withContext(Dispatchers.Main) {
                                //bindViewProcess(state.progress)
                            }
                        }

                        is DownloadState.Failed -> {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    context, "error", Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        is DownloadState.Finished -> {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    context, "done", Toast.LENGTH_SHORT
                                ).show()

                                val path = getDirFile().path + "/" + song.name
                                trackOptionViewModel.insert(musicEntity = SongEntity(id = song.id.toString(), path, song.thumbnailUrl, song.creator.name, song.name))

                                dataBinding.ivDownload.visible()
                                dataBinding.animationView.gone()
                                dataBinding.ivDownload.setImageDrawable(resources.getDrawable(R.drawable.downloaded))
                            }
                        }
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context, "error", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun requestPermission() {
        val bottomSheet = RequestPermissionBottomSheet(false)
        bottomSheet.onClickConfirmYes {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                requestManageStoragePermission()
            } else {
                requestStoragePermission()
            }
        }
        bottomSheet.show(childFragmentManager, "RequestPermissionBottomSheet")
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    fun requestStoragePermission() {
        val strArr = Constants.STORAGE_PERMISSION
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            Toast.makeText(
                requireContext(),
                "Read storage permission is required to list files",
                Toast.LENGTH_SHORT
            ).show()
        }
        requestPermissions(strArr, 1)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun requestManageStoragePermission() {
        try {
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            intent.addCategory("android.intent.category.DEFAULT")
            intent.data = Uri.parse(String.format("package:%s", requireActivity().packageName))
            requestManageStoragePermissionResultLauncher.launch(intent)
        } catch (e: Exception) {
            val intent = Intent()
            intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
            requestManageStoragePermissionResultLauncher.launch(intent)
        }
    }

    private var requestManageStoragePermissionResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Toast.makeText(
                    requireContext(),
                    "You need permission storage to use the functions!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == 0) {
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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