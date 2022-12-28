package com.example.weatherapp.ui.viewmodel

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.example.weatherapp.data.*
import com.example.weatherapp.domain.Repository
import com.example.weatherapp.domain.model.*
import com.example.weatherapp.ui.*

class MainViewModel(private val app: Application) : AndroidViewModel(app) {

    private val repository: Repository = RepositoryImpl()
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()
    fun getLiveData(): LiveData<AppState> = liveDataToObserve

    private var citiesList: List<Cities> = listOf()

    private lateinit var myBroad: MyBroadcast
    private val networkObserver: Observer<AppState> = Observer<AppState>() {
        liveDataToObserve.value = it
    };

    @RequiresApi(Build.VERSION_CODES.N)
    fun getWeather(isRus: Boolean) = getDataFromService(isRus)

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getWeatherLoader(city: Cities) {

        repository.startWeatherService(app.applicationContext, city.cityName)

    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getDataFromService(isRus: Boolean) {

        citiesList = when (isRus) {

            true -> {
                repository.getListOfRussianCities()
            }

            false -> {
                repository.getListOfWorldCities()
            }
        }
        myBroad = MyBroadcast(app.applicationContext, citiesList)
        myBroad.observeForever(networkObserver)
        getWeatherLoader(city = citiesList[0])
    }

    override fun onCleared() {
        super.onCleared()
        myBroad.removeObserver(networkObserver)
    }
}