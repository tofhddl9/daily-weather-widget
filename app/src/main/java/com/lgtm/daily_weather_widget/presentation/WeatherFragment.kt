package com.lgtm.daily_weather_widget.presentation

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.lgtm.daily_weather_widget.R
import com.lgtm.daily_weather_widget.databinding.FragmentWeatherBinding
import com.lgtm.daily_weather_widget.utils.delegate.viewBinding
import com.lgtm.daily_weather_widget.utils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WeatherFragment: Fragment(R.layout.fragment_weather) {

    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    private val binding: FragmentWeatherBinding by viewBinding(FragmentWeatherBinding::bind)

    private val viewModel: WeatherViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestPermission()

        initViews()

        observeViewModel()
    }

    private fun requestPermission() {
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            viewModel.getCurrentWeather()
        }

        permissionLauncher.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        ))
    }

    private fun initViews() {

    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    binding.loadingView.isVisible = uiState.isLoading

                    binding.title.text = uiState.uiData?.title
                    binding.address.text = uiState.uiData?.address
                    binding.message.text = uiState.uiData?.message

                    Glide.with(binding.weatherIcon)
                        .load(uiState.uiData?.weatherIcon)
                        .into(binding.weatherIcon)

                    uiState.uiData?.mainImageRes?.let {
                        binding.mainImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), it))
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.errorMessages.collect { msg ->
                    showSnackBar(msg)
                }
            }
        }

    }

}

//TODO : modify to domain vo
enum class WeatherState(
    val res: Int,
) {
//    SUNNY_DAY(R.raw.day_piece),
//    SUNNY_NIGHT(R.raw.night_piece),
//    RAINY(R.raw.day_rainy),
//    SNOWY(R.raw.day_snow),
    UNKNOWN(-1);
//
//    companion object {
//        fun getStateFrom(weather: String, isNight: Boolean): WeatherState {
//            return when (weather) {
//                "Rain" -> RAINY
//                "Snow" -> SNOWY
//                // "Clouds" ->
//                else -> {
//                    if (isNight) SUNNY_NIGHT
//                    else SUNNY_DAY
//                }
//            }
//        }
//    }
}
