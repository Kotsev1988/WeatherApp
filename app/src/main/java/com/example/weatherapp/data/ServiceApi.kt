package com.example.weatherapp.data

import com.example.weatherapp.BuildConfig
import com.example.weatherapp.model.Weather
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface ServiceApi {


    //@Headers("X-Yandex-API-Key: d35e22c6-6e49-4208-a6a0-5f36651a3dba")
    @Headers("X-Yandex-API-Key:${BuildConfig.APP_KEY}")
    @GET("v2/informers?")
    suspend fun getWeather(@Query("lat") lat: Double, @Query("lon") lon: Double, @Query("lang") lang: String = "en_US"): Response<Weather>

    companion object ServiceAPI{
        var serviceApi: ServiceApi?= null

        fun getInstance(): ServiceApi{
            if (serviceApi==null){
             /*   val retrofit: Retrofit = Retrofit.Builder()
                    .baseUrl("https://api.weather.yandex.ru/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()*/

                //использова тут Example.getRetrofit() чтобы использовать Object
                serviceApi = Example.getRetrofit().create(ServiceApi::class.java)
            }
            return serviceApi!!
        }

    }
}