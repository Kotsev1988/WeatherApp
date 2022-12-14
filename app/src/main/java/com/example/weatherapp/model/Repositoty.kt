package com.example.weatherapp.model


import retrofit2.Response

interface Repositoty {
    suspend fun getWeatherFromServer(): Response<Weather>
    fun getWeatherFromLocal(): Weather
}