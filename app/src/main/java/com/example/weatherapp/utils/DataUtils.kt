package com.example.weatherapp.utils

import android.text.BoringLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.weatherapp.domain.model.*
import com.example.weatherapp.room.HistoryEntity
import com.example.weatherapp.room.addingcity.AddCityEntities

fun convertHistoryEntityToWeather(entityList: List<HistoryEntity>): List<Weather> {
    return entityList.map {
        Weather(Current(it.feelslike_c, it.humidity, it.temp_c), Forecast(listOf()), Location(it.city))
    }
}
fun convertWeatherToEntity(weather: Weather): HistoryEntity {
    return HistoryEntity(0, weather.location.name, weather.current.humidity, weather.current.feelslike_c, weather.current.temp_c)
}

fun convertAddEntityToCity(entityList: List<AddCityEntities>): List<Cities> {
    return entityList.map {
        Cities(it.city, it.isRus)
    }
}

 fun convertAddEntityToCity1(entityList: LiveData<List<AddCityEntities>>): LiveData<List<Cities>> {



    return Transformations.map(entityList){
        entityList.value?.map {
            Cities(it.city, it.isRus)
        }
    }
}

fun convertCityToEntity(cities: Cities): AddCityEntities {
    return AddCityEntities(0, cities.cityName, cities.isRus)
}
