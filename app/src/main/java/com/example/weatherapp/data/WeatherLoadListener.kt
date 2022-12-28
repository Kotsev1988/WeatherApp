package com.example.weatherapp.data

import com.example.weatherapp.domain.model.Weather

interface WeatherLoadListener {
    fun onSuccess(weather: Weather)
    fun onFailed(throwable: Throwable)
}