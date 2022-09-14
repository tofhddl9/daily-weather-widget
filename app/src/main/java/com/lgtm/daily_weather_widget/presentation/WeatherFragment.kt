package com.lgtm.daily_weather_widget.presentation

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
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

    private val requestBackgroundLocation = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            showSnackBar("이제, 앱 위젯을 사용할 수 있습니다.")
        } else {
            showSnackBar("앱 위젯을 사용하고 싶다면, 나중에 다시 변경해주세요.")
        }
    }

    private val binding: FragmentWeatherBinding by viewBinding(FragmentWeatherBinding::bind)

    private val viewModel: WeatherViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestPermission()

        initToolbar()

        observeViewModel()
    }

    private fun requestPermission() {
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            viewModel.getCurrentWeather()
        }

        val permissionList = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        permissionLauncher.launch(permissionList)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            requireContext().checkBackgroundLocationPermission()
        }
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

    private fun initToolbar() = with(binding.toolbar) {
        inflateMenu(R.menu.weather_menu)
        setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_setting -> {
                    moveToSetting()
                    return@setOnMenuItemClickListener true
                }
                else -> {
                    false
                }
            }
        }
    }

    private fun moveToSetting() {
        findNavController().navigate(WeatherFragmentDirections.actionWeatherFragmentToSettingFragment())
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun Context.checkBackgroundLocationPermission() {
        if (checkSinglePermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)) return
        AlertDialog.Builder(this)
            .setTitle("위치 권한 설정 안내")
            .setMessage("앱 위젯을 사용하고 싶다면, 위치 권한을 \"항상 허용\"으로 설정해주세요")
            .setPositiveButton("설정하기") { _, _ ->
                requestBackgroundLocation.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            }
            .setNegativeButton("닫기") { dialog,_ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun Context.checkSinglePermission(permission: String) : Boolean {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }

}
