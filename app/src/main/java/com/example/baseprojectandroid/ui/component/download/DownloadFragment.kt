package com.example.baseprojectandroid.ui.component.download

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.databinding.FragmentDownloadBinding
import com.example.baseprojectandroid.extension.downloadFile
import com.example.baseprojectandroid.model.DownloadState
import com.example.baseprojectandroid.model.Music
import com.example.baseprojectandroid.ui.base.BaseFragmentBinding
import com.example.baseprojectandroid.ui.component.splash.RequestPermissionBottomSheet
import com.example.baseprojectandroid.utils.Constants
import com.example.baseprojectandroid.utils.MusicUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class DownloadFragment : BaseFragmentBinding<FragmentDownloadBinding, DownloadViewModel>() {

    override fun getContentViewId() = R.layout.fragment_download

    private lateinit var adapter: DownloadAdapter

    override fun initializeViews() {
        adapter = DownloadAdapter(requireContext())

        var list = ArrayList<Music>()
        list.add(Music("bo"))
        list.add(Music("ung qua chung"))
        list.add(Music("meo meo meo meo"))
        list.add(Music("Tha thứ lỗi lầm"))
        list.add(Music("Lần cuối"))
        Log.d("buituyen", list.size.toString())
        adapter.submitList(list,true)

        dataBinding.recyclerView.adapter = adapter
    }

    override fun registerListeners() {
        adapter.setOnItemClickListener { position ->
            handleClickButtonDownload()
        }
    }

    override fun initializeData() {

    }

    override fun registerObservers() {

    }

    private fun bindViewProcess(progress: Int) {
        /*dataBinding.progressBar3.progress = progress
        dataBinding.processTxt.text = "$progress %"*/
    }

    private fun handleClickButtonDownload() {
        if (!MusicUtils.checkPermission(requireContext())) {
            requestPermission()
            return
        }
        CoroutineScope(Dispatchers.IO).launch {
            val flow = context?.let {
                downloadFile(
                    "http://ec2-3-106-133-27.ap-southeast-2.compute.amazonaws.com:8080/api/Songs/musicFiles/DuaEmVeNhaa-GREYDChillies-9214678.mp3",
                    it,
                    "duaemvenha$.mp3"
                )
            }
            if (flow != null) {
                flow.collect { state ->
                    when (state) {
                        is DownloadState.Downloading -> {
                            withContext(Dispatchers.Main) {
                                bindViewProcess(state.progress)
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
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
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
            intent.data =
                Uri.parse(String.format("package:%s", requireActivity().packageName))
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
            if (Environment.isExternalStorageManager()) {

            } else {

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
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == 0) {            return
            return
        } else {

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}