package com.example.baseprojectandroid.ui.component.library.profile

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.GradientDrawable
import androidx.palette.graphics.Palette
import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.databinding.FragmentProfileBinding
import com.example.baseprojectandroid.ui.base.BaseFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragmentBinding<FragmentProfileBinding, ProfileViewModel>() {

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

    }

    override fun initializeData() {

    }

    override fun registerObservers() {

    }
}