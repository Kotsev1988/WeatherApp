package com.example.weatherapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentWeatherBinding
import com.example.weatherapp.ui.viewmodel.AppState
import com.example.weatherapp.ui.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherFragment : Fragment(R.layout.fragment_weather) {
    private var _binding: FragmentWeatherBinding? = null
    private lateinit var viewModel: MainViewModel
    private val binding
        get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentWeatherBinding.bind(view)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer {
            getAppState(it)
        })

        CoroutineScope(Dispatchers.IO).launch {
            viewModel.getWeather()
        }
    }


    private fun getAppState(it: AppState) {
        when (it) {

            is AppState.Success -> {
                binding.frameLoading.visibility = View.GONE
                val weatherData = it.weather
                binding.cityName.text = weatherData.location.name
                binding.currentTemperature.text = weatherData.current.temp_c.toString() + " °C"
                binding.humidity.text = "Влажность : " + weatherData.current.humidity.toString()
                Glide.with(this).asBitmap()
                    .load("http://" + weatherData.current.condition.icon)
                    .circleCrop().into(binding.weatherIcon)
            }
            is AppState.Loading -> {
                binding.frameLoading.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.frameLoading.visibility = View.GONE
                val error = it.error
                Snackbar.make(binding.mainView, error, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

}