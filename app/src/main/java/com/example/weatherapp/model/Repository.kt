package com.example.weatherapp.model


import retrofit2.Response

interface Repository {
    suspend fun getWeatherFromServer(city: String): Response<Weather>
    fun getWeatherFromLocal(): Weather
    fun getRussianCities(): List<Cities>
    fun getWorldCities(): List<Cities>
    fun getOne(): Int
}