package com.example.weatherapp.data

import com.example.weatherapp.BuildConfig
import com.example.weatherapp.model.Info
import com.example.weatherapp.model.Weather
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

object Example {


    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.weather.yandex.ru/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

   private val infoNalchik: Map<String, Info> =
       hashMapOf<String, Info>( "Nalchik" to Info( 43.4981 , 43.6189, "en_US"), "Moscow" to Info( 55.75396 , 37.620393, "en_US"))

    private val infoMoscow: ArrayList<Info> = arrayListOf(Info( 55.75396 , 37.620393, "en_US"), Info( 43.4981 , 43.6189, "en_US"))

    fun getCity(info: String): Info? {

        return infoNalchik.get(info)
    }

    fun getCities(): ArrayList<Info>{
        return infoMoscow
    }

    fun getRetrofit(): Retrofit{
        return retrofit
    }

    fun fun2(){
        for (i in 0..10){
            println("Цикл i = "+i);
        }
    }

    fun getCityName( lat: Double,  lon: Double): String{
        infoNalchik.forEach {
            if (lat == it.value.lat && lon == it.value.lon){
                return it.key
            }
        }
        return ""
    }

    fun fun3(){
        for (i in 0..10){
            println("Цикл i = "+i);
        }
    }



}