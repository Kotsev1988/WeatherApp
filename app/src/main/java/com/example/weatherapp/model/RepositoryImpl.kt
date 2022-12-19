package com.example.weatherapp.model

import com.example.weatherapp.data.RetrofitCleint
import retrofit2.Response

class RepositoryImpl : Repository {
    private val service = RetrofitCleint.getInstance()
    override suspend fun getWeatherFromServer(city: String): Response<Weather> = service.getWeather(city = city)

    override fun getWeatherFromLocal(): Weather {
        TODO("Not yet implemented")
    }

    override fun getRussianCities(): List<Cities> = getRussianCities()

    override fun getWorldCities(): List<Cities> = getWorldCities()

    override fun getOne(): Int {
        return 1
    }
}