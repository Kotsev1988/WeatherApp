package com.example.weatherapp.ui.viewmodel

import com.example.weatherapp.domain.model.Weather

sealed class AppStateForCity {
    data class Success(val weather: Weather?) : AppStateForCity()
    data class Error(val error: Throwable) : AppStateForCity()
    data class EmptyData(val message: String) : AppStateForCity()
    object Loading : AppStateForCity()
}