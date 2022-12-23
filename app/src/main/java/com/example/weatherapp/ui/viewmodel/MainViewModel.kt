package com.example.weatherapp.ui.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.domain.Repository
import com.example.weatherapp.data.RepositoryImpl
import com.example.weatherapp.data.WeatherLoadListener
import com.example.weatherapp.data.WeatherLoader
import com.example.weatherapp.domain.model.Cities
import com.example.weatherapp.domain.model.Weather

class MainViewModel : ViewModel() {

    private val repository: Repository = RepositoryImpl()
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()

    fun getLiveData(): LiveData<AppState> = liveDataToObserve

    @RequiresApi(Build.VERSION_CODES.N)
    fun getWeather(isRus: Boolean) = getDataFromService(isRus)

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getWeatherLoader(city: List<Cities>) {
        val loader = WeatherLoader(object : WeatherLoadListener {
            override fun onSuccess(weather: Weather) {
                liveDataToObserve.value =
                    AppState.Success(weather = weather, city)
            }

            override fun onFailed(throwable: Throwable) {
                liveDataToObserve.value = AppState.Error(error = throwable.message.toString())
            }

        }, city = city[0].cityName)
        loader.loadWeather()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getDataFromService(isRus: Boolean) {

        val cities: List<Cities> = when (isRus) {

            true -> {
                repository.getListOfRussianCities()
            }

            false -> {
                repository.getListOfWorldCities()
            }
        }
        getWeatherLoader(city = cities)
    }
}