package com.example.weatherapp.domain.model.repository.history

import com.example.weatherapp.domain.model.Weather

interface LocalRepository {
    fun getAllHistory(): List<Weather>
    fun getFilterData(s: String): List<Weather>
    fun saveEntity(weather: Weather)

}