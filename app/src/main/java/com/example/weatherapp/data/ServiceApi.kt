package com.example.weatherapp.data

import com.example.weatherapp.BuildConfig
import com.example.weatherapp.model.Weather
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ServiceApi {

    //@Headers("X-Yandex-API-Key:${BuildConfig.APP_KEY}") 6af6d40b518c49b0b9e90426221312
    //@Headers("X-Yandex-API-Key:54e65b88-aeba-4438-b342-6f682b908ff2")
    @GET("v1/current.json?")
    suspend fun getWeather(
        @Query("key") key: String = BuildConfig.APP_KEY,
        @Query("q") city: String= "Москва",
        @Query("aqi") aqi: String = "no",
    ): Response<Weather>

    companion object ServiceAPI {
        var serviceApi: ServiceApi? = null

        fun getInstance(): ServiceApi {
            if (serviceApi == null) {
                val retrofit: Retrofit = Retrofit.Builder()
                    .baseUrl("http://api.weatherapi.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                serviceApi = retrofit.create(ServiceApi::class.java)
            }
            return serviceApi!!
        }

    }
}