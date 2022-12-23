package com.example.weatherapp.ui.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.RepositoryImpl
import com.example.weatherapp.data.WeatherLoadListener
import com.example.weatherapp.data.WeatherLoader
import com.example.weatherapp.domain.Repository
import com.example.weatherapp.domain.model.Cities
import com.example.weatherapp.domain.model.Weather
import com.example.weatherapp.domain.model.getRussianCities
import com.example.weatherapp.domain.model.getWorldCities
import kotlinx.coroutines.launch

class ViewModelCity : ViewModel() {

    private val repository : Repository = RepositoryImpl()
    private val liveDataToObserve : MutableLiveData<AppStateForCity> = MutableLiveData()

    fun getLiveData() : LiveData<AppStateForCity> = liveDataToObserve

    @RequiresApi(Build.VERSION_CODES.N)
     fun getWeather(city : String) = getDataFromService(city)

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getWeatherLoader(city: String) {
        val loader = WeatherLoader(object : WeatherLoadListener {
            override fun onSuccess(weather: Weather) {
                liveDataToObserve.value =
                    AppStateForCity.Success(weather = weather)
            }

            override fun onFailed(throwable: Throwable) {
                liveDataToObserve.value = AppStateForCity.Error(error = throwable.message.toString())
            }

        }, city = city)
        loader.loadWeather()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private  fun getDataFromService(city: String) {

        getWeatherLoader(city)

       /* viewModelScope.launch {


                val weather = repository.getWeatherFromServer(city)
                if (weather.isSuccessful) {
                    liveDataToObserve.value = AppStateForCity.Success(weather = weather.body())
                } else {
                    liveDataToObserve.value = AppStateForCity.Error(error = weather.message())
                }



        }*/
    }
}