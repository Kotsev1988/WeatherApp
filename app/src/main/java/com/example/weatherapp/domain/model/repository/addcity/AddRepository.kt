package com.example.weatherapp.domain.model.repository.addcity

import androidx.lifecycle.LiveData
import com.example.weatherapp.domain.model.Cities

interface AddRepository {

    fun getAll(): List<Cities>
    fun getListOfRussianCities() : List<Cities>
    fun getListOfWorldCities() : List<Cities>

    fun getListOfRussianCities1() : LiveData<List<Cities>>
    fun getListOfWorldCities1() : LiveData<List<Cities>>


    fun addCity(cities: Cities)
}