package com.example.weatherapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentCityWeatherBinding
import com.example.weatherapp.domain.model.Cities
import com.example.weatherapp.domain.model.Weather
import com.example.weatherapp.ui.viewmodel.appSatets.AppStateForCity
import com.example.weatherapp.ui.viewmodel.ViewModelCity
import com.example.weatherapp.ui.viewmodel.appSatets.AppStateGetGoesByName
import com.google.android.material.snackbar.Snackbar


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
        context?.let {
            viewModel.getAddressAsync(it, city)
        }


        viewModel.getLiveData().observe(viewLifecycleOwner, Observer { appSate ->
            getData(appSate)
        })

        viewModel.getLiveDataLocation().observe(viewLifecycleOwner, Observer {
            getLocationData(it)
        })
    }

    private fun getLocationData(it: AppStateGetGoesByName) {

        when (it) {

            is AppStateGetGoesByName.Success -> {

                    binding.geoData.text = it.location.get(0).latitude.toString() + " " +
                            it.location.get(0).longitude.toString()

            }
            is AppStateGetGoesByName.Error-> {
                Toast.makeText(context, it.error.message, Toast.LENGTH_SHORT).show()
            }

        }
    }

    fun getData(appSate: AppStateForCity) {

        when (appSate) {
            is AppStateForCity.Success -> {
                binding.cityLoading.visibility = View.GONE

                appSate.weather.let { weather ->

                    saveCity(weather)
                    weather?.current.also {

                        binding.temperatureInCity.text = it?.temp_c.toString()
                        binding.feeling.text = it?.feelslike_c.toString()
                        binding.humidity.text = it?.humidity.toString()

                        Glide.with(this)
                            .load("https://freepngimg.com/thumb/city/36275-3-city-hd.png")
                            .into(binding.imageCity);
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
                Snackbar.make(binding.cityView, error.message.toString(), Snackbar.LENGTH_SHORT)
                    .show()
            }
            is AppStateForCity.EmptyData -> {
                Toast.makeText(requireActivity(), appSate.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveCity(weather: Weather?) {

        if (weather != null) {
            viewModel.saveCityToDB(weather)
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