package com.example.weatherapp.data

import com.example.weatherapp.BuildConfig
import com.example.weatherapp.domain.model.Weather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ServiceApi {

    @GET("v1/forecast.json?")
    suspend fun getWeather(
        @Query("key") key: String = BuildConfig.WEATHER_API_KEY,
        @Query("q") city: String,
        @Query("days") days: String= "1",
        @Query("aqi") aqi: String = "no",
        @Query("alerts") no: String= "no",
        @Query("lang") lang: String= "ru"
    ) : Response<Weather>

}