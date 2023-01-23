package com.example.weatherapp.domain.model.repository

import com.example.weatherapp.domain.model.Cities
import com.example.weatherapp.domain.model.Weather
import retrofit2.Callback

interface Repository {
     fun getWeatherFromServer(city: String, call: Callback<Weather>)
    fun getListOfRussianCities() : List<Cities>
    fun getListOfWorldCities() : List<Cities>

}