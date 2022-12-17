package com.example.weatherapp.ui.viewmodel

import com.example.weatherapp.model.Cities
import com.example.weatherapp.model.Weather

sealed class AppState {
    data class Success(val weather: Weather, val cities: List<Cities>) : AppState()
    data class Error(val error: String) : AppState()
    object Loading : AppState()
}