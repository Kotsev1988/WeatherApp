package com.example.weatherapp.ui.addcity

import androidx.lifecycle.ViewModel
import com.example.weatherapp.App
import com.example.weatherapp.domain.model.Cities
import com.example.weatherapp.domain.model.repository.addcity.AddRepository
import com.example.weatherapp.domain.model.repository.addcity.AddRepositoryImpl

class AddCityViewModel(
    private val historyRepository: AddRepository = AddRepositoryImpl(App.getCitiesDao())
): ViewModel() {

    fun addCity(cityName: String, isRus: Boolean){
        historyRepository.addCity(Cities(cityName, isRus))
    }
}