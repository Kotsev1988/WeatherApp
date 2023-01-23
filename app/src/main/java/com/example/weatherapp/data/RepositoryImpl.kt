package com.example.weatherapp.data


import com.example.weatherapp.domain.model.repository.Repository
import com.example.weatherapp.domain.model.Cities
import com.example.weatherapp.domain.model.Weather
import com.example.weatherapp.domain.model.getRussianCities
import com.example.weatherapp.domain.model.getWorldCities
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RepositoryImpl(private val service: RetrofitCleint) : Repository {

    override  fun getWeatherFromServer(city: String, callback: Callback<Weather>) {
        service.getWeather(city, callback)
    }

    override fun getListOfRussianCities(): List<Cities> = getRussianCities()

    override fun getListOfWorldCities(): List<Cities> = getWorldCities()

}