package com.example.weatherapp.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitCleint {

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