package com.example.weatherapp.ui

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentWeatherBinding
import com.example.weatherapp.domain.model.Cities
import com.example.weatherapp.domain.model.getRussianCities
import com.example.weatherapp.domain.model.getWorldCities
import com.example.weatherapp.ui.viewmodel.AppState
import com.example.weatherapp.ui.viewmodel.MainViewModel
import com.example.weatherapp.ui.adapters.WeatherHorizontalDay
import com.example.weatherapp.ui.adapters.WeatherRecyclerAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_weather.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherFragment : Fragment(R.layout.fragment_weather) {
    private var _binding: FragmentWeatherBinding? = null

    private val adapter = WeatherRecyclerAdapter(object : OnItemClickListener {
        override fun onItemClick(cities: Cities) {

            activity?.supportFragmentManager?.apply {
                val bundle = Bundle()
                bundle.putParcelable(CityWeather.BUNDLE_EXTRA, cities)

                beginTransaction()
                    .add(R.id.container, CityWeather.newInstance(bundle))
                    .addToBackStack("")
                    .commitAllowingStateLoss()
            }
        }
    })
    private val adapterOfDay = WeatherHorizontalDay(this)
    private val binding
        get() = _binding!!

    private var isRussianCities: Boolean = true

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    interface OnItemClickListener {
        fun onItemClick(cities: Cities)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentWeatherBinding.bind(view)

        binding.mainFragmentFAB.setOnClickListener {
            changeWeatherData()
        }

        WeatherRecycler.adapter = adapter
        weatherHorizontal.adapter = adapterOfDay

        viewModel.getLiveData().observe(viewLifecycleOwner, Observer {
            getAppState(it)
        })

        viewModel.getWeather(isRussianCities)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private  fun changeWeatherData() {

        isRussianCities = !isRussianCities
        viewModel.getWeather(isRussianCities)

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getAppState(it: AppState) {
        when (it) {

            is AppState.Success -> {

                binding.frameLoading.visibility = View.GONE

                val weather = it.weather
                val cities = it.cities

                binding.city.text = cities.get(0).cityName

                adapter.setData(cities)
                weather?.forecast?.forecastday?.get(0)?.hour?.let { it1 -> adapterOfDay.setData(it1) }
            }

            is AppState.Loading -> {
                binding.frameLoading.visibility = View.VISIBLE
            }

            is AppState.Error -> {
                binding.frameLoading.visibility = View.GONE
                val error = it.error

                binding.mainView.showSnackBar(
                    error,
                    getString(R.string.reload),

                    {
                        viewModel.getDataFromService(isRussianCities)
                    }
                )
            }
        }
    }

    override fun onDestroy() {
        adapter.removeListener()
        super.onDestroy()
    }

    private fun View.showSnackBar(
        text: String,
        actionText: String,
        action: (View) -> Unit,
        length: Int = Snackbar.LENGTH_INDEFINITE,
    ) {
        Snackbar.make(this, text, length).setAction(actionText, action).show()
    }
}


