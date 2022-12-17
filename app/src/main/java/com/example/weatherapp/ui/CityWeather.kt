package com.example.weatherapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentCityWeatherBinding
import com.example.weatherapp.model.Cities
import com.example.weatherapp.ui.viewmodel.AppState
import com.example.weatherapp.ui.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CityWeather : Fragment(R.layout.fragment_city_weather) {
    private lateinit var viewModel: MainViewModel
    private var _binding: FragmentCityWeatherBinding? = null
    private val binding
        get() = _binding!!
    private var cityName: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_city_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCityWeatherBinding.bind(view)
        val weather = arguments?.getParcelable<Cities>(BUNDLE_EXTRA)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer {
            getData(it)
        })

        if (weather != null) {

            cityName = weather.cityName
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.getWeather(cityName, false)
            }
        }
    }

    fun getData(it: AppState) {

        when (it) {
            is AppState.Success -> {
                val weather = it.weather
                binding.cityLoading.visibility = View.GONE
                binding.nameOfCity.text = weather.location.name
                binding.temperatureInCity.text = weather.current.temp_c.toString()
            }
            is AppState.Loading -> {
                binding.cityLoading.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.cityLoading.visibility = View.GONE
                val error = it.error
                Snackbar.make(binding.cityView, error, Snackbar.LENGTH_SHORT).show()
            }
        }

    }

    companion object {
        const val BUNDLE_EXTRA = "weather"
        fun newInstance(bundle: Bundle): CityWeather {
            val fragment = CityWeather()
            fragment.arguments = bundle
            return fragment
        }
    }
}