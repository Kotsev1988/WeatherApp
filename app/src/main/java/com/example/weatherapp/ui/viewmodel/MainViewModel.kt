package com.example.weatherapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.RepositoryImpl
import com.example.weatherapp.model.Repositoty
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val repository: Repositoty = RepositoryImpl()
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()

    fun getLiveData(): LiveData<AppState> = liveDataToObserve

    suspend fun getWeather() = getDataFromService()

    private suspend fun getDataFromService() {

        viewModelScope.launch {
            val weather = repository.getWeatherFromServer()
            if (weather.isSuccessful) {
                liveDataToObserve.value = AppState.Success(weather = weather.body()!!)
            } else {
                liveDataToObserve.value = AppState.Error(error = weather.message())
            }
        }
    }
}