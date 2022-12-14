package com.example.weatherapp.model

import com.example.weatherapp.data.ServiceApi
import retrofit2.Response

class RepositoryImpl : Repositoty {
    private val service = ServiceApi.getInstance()
    override suspend fun getWeatherFromServer(): Response<Weather> {
        return service.getWeather()
    }

    override fun getWeatherFromLocal(): Weather {
        TODO("Not yet implemented")
    }


}