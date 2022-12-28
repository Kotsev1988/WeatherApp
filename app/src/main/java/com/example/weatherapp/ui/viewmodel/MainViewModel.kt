package com.example.weatherapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.*
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val repository: Repository = RepositoryImpl()
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()

    fun getLiveData(): LiveData<AppState> = liveDataToObserve

    suspend fun getWeather(city: String, isRus: Boolean) = getDataFromService(city, isRus)

    private suspend fun getDataFromService(city: String, isRus: Boolean) {

        viewModelScope.launch {
            val weather = repository.getWeatherFromServer(city)
            if (weather.isSuccessful) {
                if (isRus) {
                    liveDataToObserve.value =
                        AppState.Success(weather = weather.body()!!, repository.getListOfRussianCities())
                } else {
                    liveDataToObserve.value =
                        AppState.Success(weather = weather.body()!!, repository.getListOfWorldCities())
                }
            }
            else
            {
                liveDataToObserve.value = AppState.Error(error = weather.message())
            }
        }
    }
}
