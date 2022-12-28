package com.example.weatherapp.ui.viewmodel

import com.example.weatherapp.domain.model.Cities
import com.example.weatherapp.domain.model.Weather

sealed class AppStateForCity {
    data class Success(val weather: Weather?) : AppStateForCity()
    data class Error(val error: String) : AppStateForCity()
    data class EmptyData(val message: String) : AppStateForCity()
    object Loading : AppStateForCity()
}