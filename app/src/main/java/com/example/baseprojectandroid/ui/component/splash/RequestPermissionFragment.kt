package com.example.baseprojectandroid.ui.component.splash

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.databinding.FragmentRequestPermissonBinding
import com.example.baseprojectandroid.ui.base.BaseFragment
import com.example.baseprojectandroid.utils.Constants

class RequestPermissionFragment : BaseFragment() {
    private lateinit var binding: FragmentRequestPermissonBinding

    override fun getContentViewId() = R.layout.fragment_request_permisson

    override fun initializeViews() {
        binding = FragmentRequestPermissonBinding.bind(mView!!)
    }

    override fun registerListeners() {
        binding.btnCancel.setOnClickListener {
            goToHomeFragment()
        }
        binding.btnContinue.setOnClickListener {
            handleRequestPermission()
        }
    }

    override fun initializeData() {

    }

    //----------------------------------------------------------------------------------------------
    private var requestManageStoragePermissionResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                goToHomeFragment()
            } else {
                goToHomeFragment()
                Toast.makeText(
                    requireContext(),
                    "You need permission storage to use the functions!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun handleRequestPermission() {
        if (checkPermission(requireContext())) {
            goToHomeFragment()
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requestManageStoragePermission()
        } else {
            requestStoragePermission()
        }
    }

    fun checkPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            val result: Int =
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            val result1: Int =
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
        }
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

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == 0) {
            goToHomeFragment()
            return
        } else {
            goToHomeFragment()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun goToHomeFragment() {
        findNavController().navigate(R.id.requestPermissionFragment)
    }

}