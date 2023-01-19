package com.example.weatherapp.ui.viewmodel

import com.example.weatherapp.domain.model.Cities
import com.example.weatherapp.domain.model.Weather

sealed class AppState {
    data class Success(val weather: Weather?, val cities: List<Cities>) : AppState()
    data class Error(val error: Throwable) : AppState()
    data class EmptyData(val message: String) : AppState()
    object Loading : AppState()
}