package com.example.weatherapp.ui

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentCityWeatherBinding
import com.example.weatherapp.domain.model.Cities
import com.example.weatherapp.ui.viewmodel.AppState
import com.example.weatherapp.ui.viewmodel.AppStateForCity
import com.example.weatherapp.ui.viewmodel.MainViewModel
import com.example.weatherapp.ui.viewmodel.ViewModelCity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_city_weather.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CityWeather : Fragment(R.layout.fragment_city_weather) {

    private val viewModel: ViewModelCity by lazy {
        ViewModelProvider(this)[ViewModelCity::class.java]
    }
    private var _binding: FragmentCityWeatherBinding? = null
    private val binding
        get() = _binding!!
    private var city: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_city_weather, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCityWeatherBinding.bind(view)

        arguments?.getParcelable<Cities>(BUNDLE_EXTRA).let { weather ->
            city = weather?.cityName.toString()
        }.also {
            viewModel.getWeather(city)
        } ?: kotlin.run {
            Toast.makeText(requireContext(), "No Any Parcelable", Toast.LENGTH_SHORT)
        }

        viewModel.getLiveData().observe(viewLifecycleOwner, Observer { appSate ->
            getData(appSate)
        })
    }

    fun getData(appSate: AppStateForCity) {

        when (appSate) {
            is AppStateForCity.Success -> {
                binding.cityLoading.visibility = View.GONE
                appSate.weather.let { weather ->

                    weather?.current.also {

                        binding.temperatureInCity.text = it?.temp_c.toString()
                        binding.feeling.text = it?.feelslike_c.toString()
                        binding.humidity.text = it?.humidity.toString()

                    }
                    binding.nameOfCity.text = weather?.location?.name
                }
            }
            is AppStateForCity.Loading -> {
                binding.cityLoading.visibility = View.VISIBLE
            }
            is AppStateForCity.Error -> {
                binding.cityLoading.visibility = View.GONE
                val error = appSate.error
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