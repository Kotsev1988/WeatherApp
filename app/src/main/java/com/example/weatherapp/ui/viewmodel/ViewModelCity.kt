package com.example.weatherapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.RepositoryImpl
import com.example.weatherapp.domain.Repository
import com.example.weatherapp.domain.model.getRussianCities
import com.example.weatherapp.domain.model.getWorldCities
import kotlinx.coroutines.launch

class ViewModelCity : ViewModel() {

    private val repository : Repository = RepositoryImpl()
    private val liveDataToObserve : MutableLiveData<AppStateForCity> = MutableLiveData()

    fun getLiveData() : LiveData<AppStateForCity> = liveDataToObserve

    suspend fun getWeather(city : String) = getDataFromService(city)

    private suspend fun getDataFromService(city: String) {

        viewModelScope.launch {


                val weather = repository.getWeatherFromServer(city)
                if (weather.isSuccessful) {
                    liveDataToObserve.value = AppStateForCity.Success(weather = weather.body())
                } else {
                    liveDataToObserve.value = AppStateForCity.Error(error = weather.message())
                }



        }
    }
}