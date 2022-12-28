package com.example.weatherapp.ui.viewmodel

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.example.weatherapp.data.RepositoryImpl
import com.example.weatherapp.domain.Repository
import com.example.weatherapp.ui.adapters.MyBroadcastForCity

class ViewModelCity(private val app: Application) : AndroidViewModel(app) {

    private val repository : Repository = RepositoryImpl()
    private val liveDataToObserve : MutableLiveData<AppStateForCity> = MutableLiveData()

    fun getLiveData() : LiveData<AppStateForCity> = liveDataToObserve

    private lateinit var myBroad: MyBroadcastForCity
    private val networkObserver: Observer<AppStateForCity> = Observer<AppStateForCity>() {
        liveDataToObserve.value = it
    };

    @RequiresApi(Build.VERSION_CODES.N)
     fun getWeather(city : String) = getDataFromService(city)

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getWeatherLoader(city: String) {
        repository.startWeatherService(app.applicationContext, city)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private  fun getDataFromService(city: String) {

        myBroad = MyBroadcastForCity(app.applicationContext)
        myBroad.observeForever(networkObserver)
        getWeatherLoader(city = city)
    }
}