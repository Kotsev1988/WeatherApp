package com.example.weatherapp.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentWeatherBinding
import com.example.weatherapp.domain.model.*
import com.example.weatherapp.ui.viewmodel.AppState
import com.example.weatherapp.ui.viewmodel.MainViewModel
import com.example.weatherapp.ui.adapters.WeatherHorizontalDay
import com.example.weatherapp.ui.adapters.WeatherRecyclerAdapter
import com.example.weatherapp.ui.addcity.AddCityFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_weather.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val IS_WORLD_KEY = "LIST_OF_TOWNS_KEY"

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentWeatherBinding.bind(view)

        showListOfTowns()

        binding.mainFragmentFAB.setOnClickListener {
            changeWeatherData()
        }

        WeatherRecycler.adapter = adapter
        weatherHorizontal.adapter = adapterOfDay

        viewModel.getLiveData().observe(viewLifecycleOwner, Observer {
            getAppState(it)
        })

        viewModel.getLiveDataCities().observe(viewLifecycleOwner, Observer {

            adapter.setData(it)
        })

        viewModel.updateCitites()
        viewModel.getWeather(isRussianCities)

    }

    private fun showListOfTowns() {
        activity?.let {
            if (it.getPreferences(Context.MODE_PRIVATE).getBoolean(IS_WORLD_KEY,
                    false)
            ) {
                changeWeatherData()
            } else {
                viewModel.getDataFromService(isRussianCities)
            }
        }
    }

    private fun saveListOfTowns() {
        activity?.let {
            with(it.getPreferences(Context.MODE_PRIVATE).edit()) {
                putBoolean(IS_WORLD_KEY, !isRussianCities)
                apply()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        println("MainFragment Pause")
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
        println("MainFragment onStop")
    }

    override fun onStart() {
        super.onStart()
        println("MainFragment onStart")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        println("MainFragment Atach")
    }

    override fun onDetach() {
        super.onDetach()
        println("MainFragment Detach")
    }

    private fun changeWeatherData() {

        isRussianCities = !isRussianCities
        viewModel.getWeather(isRussianCities)

        saveListOfTowns()

    }


    private fun getAppState(it: AppState) {
        when (it) {

            is AppState.Success -> {

                binding.frameLoading.visibility = View.GONE

                val weather = it.weather
                val cities = it.cities

                binding.city.text = cities.get(0).cityName
                weather?.forecast?.forecastday?.get(0)?.hour?.let { it1 -> adapterOfDay.setData(it1) }
            }

            is AppState.Loading -> {
                binding.frameLoading.visibility = View.VISIBLE
            }

            is AppState.EmptyData -> {
                Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
            }

            is AppState.Error -> {
                binding.frameLoading.visibility = View.GONE
                val error = it.error

                error.message?.let { it1 ->
                    binding.mainView.showSnackBar(
                        it1,
                        getString(R.string.reload),

                        {
                            viewModel.getDataFromService(isRussianCities)
                        }
                    )
                }
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


