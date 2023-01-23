package com.example.weatherapp.domain.model.repository.history

import com.example.weatherapp.domain.model.Weather

interface LocalRepository {
    fun getAllHistory(): List<Weather>
    fun saveEntity(weather: Weather)

}