package com.example.weatherapp.ui.viewmodel

import com.example.weatherapp.model.Weather

sealed class AppState {
    data class Success(val weather: Weather) : AppState()
    data class Error(val error: String) : AppState()
    object Loading : AppState()
}