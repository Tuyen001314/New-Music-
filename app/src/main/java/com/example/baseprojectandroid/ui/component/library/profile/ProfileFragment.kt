package com.example.baseprojectandroid.ui.component.library.profile

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.GradientDrawable
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.palette.graphics.Palette
import com.example.baseprojectandroid.App
import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.databinding.FragmentProfileBinding
import com.example.baseprojectandroid.ui.base.BaseFragmentBinding
import com.example.baseprojectandroid.ui.base.BaseViewModel
import com.example.baseprojectandroid.ui.component.library.UploadTrackViewModel
import com.example.baseprojectandroid.ui.component.library.upload.UploadTrackFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragmentBinding<FragmentProfileBinding, BaseViewModel>() {

    private val profileViewModel by activityViewModels<ProfileViewModel>()
    override fun getContentViewId() = R.layout.fragment_profile

    override fun initializeViews() {

        val imageView = dataBinding.imgProfile
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap

        Palette.from(bitmap).generate { palette ->
            val dominantColor = palette?.dominantSwatch?.rgb
            val vibrantColor = palette?.vibrantSwatch?.rgb
            val mutedColor = palette?.mutedSwatch?.rgb
            val lightVibrantColor = palette?.lightVibrantSwatch?.rgb
            val darkVibrantColor = palette?.darkVibrantSwatch?.rgb
            val lightMutedColor = palette?.lightMutedSwatch?.rgb
            val darkMutedColor = palette?.darkMutedSwatch?.rgb

            if (darkVibrantColor != null && dominantColor != null) {
                val gradientDrawable = GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM,
                    intArrayOf(
                        darkVibrantColor,
                        dominantColor,
                        resources.getColor(R.color.bg_app)
                    )
                )
                gradientDrawable.cornerRadius = 0f
                gradientDrawable.gradientType = GradientDrawable.LINEAR_GRADIENT
                gradientDrawable.let { dataBinding.bgProfileTop.background = gradientDrawable }
            }
        }
    }

    override fun registerListeners() {
        dataBinding.btnChangeAvatar.setOnClickListener {
            openImageChooser()
        }
    }

    override fun initializeData() {

    }

    override fun registerObservers() {
        profileViewModel.currentImageFile.observe(viewLifecycleOwner) {
            it?.let { image ->
                dataBinding.imgProfile.setImageURI(image.toUri())
                profileViewModel.changeAvatar(81)
            }
        }
    }

    private fun openImageChooser() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(it, REQUEST_CODE_PICK_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_PICK_IMAGE -> {
                    profileViewModel.onImageUriSelected(requireContext(), data!!.data!!)
                }
            }
        }
    }

    companion object {
        const val REQUEST_CODE_PICK_IMAGE = 101
    }
}