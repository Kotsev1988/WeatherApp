package com.example.weatherapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.domain.Repository
import com.example.weatherapp.data.RepositoryImpl
import com.example.weatherapp.domain.model.Cities
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val repository: Repository = RepositoryImpl()
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()

    fun getLiveData(): LiveData<AppState> = liveDataToObserve

    suspend fun getWeather(isRus: Boolean) = getDataFromService(isRus)

    private suspend fun getDataFromService(isRus: Boolean) {
        var cities: List<Cities>
        viewModelScope.launch {
            cities = when(isRus){
                true -> {
                    repository.getListOfRussianCities()
                }
                false -> {
                    repository.getListOfWorldCities()
                }
            }

            val weather = repository.getWeatherFromServer(cities[0].cityName)
            if (weather.isSuccessful) {
                liveDataToObserve.value =
                    AppState.Success(weather = weather.body(), cities)
            } else {
                liveDataToObserve.value = AppState.Error(error = weather.message())
            }
        }
    }
}