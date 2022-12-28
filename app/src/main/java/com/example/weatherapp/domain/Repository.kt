package com.example.weatherapp.domain


import com.example.weatherapp.data.WeatherLoadListener
import com.example.weatherapp.data.WeatherLoader
import com.example.weatherapp.domain.model.Cities
import com.example.weatherapp.domain.model.Weather
import retrofit2.Response

interface Repository {
    suspend fun getWeatherFromServer(city: String) : Response<Weather>

    fun getWeatherFromLocal() : Weather
    fun getListOfRussianCities() : List<Cities>
    fun getListOfWorldCities() : List<Cities>
     fun getWeatherFromLoader(listener: WeatherLoadListener, city: String) : WeatherLoader
}