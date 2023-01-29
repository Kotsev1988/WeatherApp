package com.example.weatherapp.ui.viewmodel.appSatets


import com.example.weatherapp.domain.model.Weather

sealed class AppStateHistory{

    data class Success(val weather: List<Weather>) : AppStateHistory()
    data class Error(val error: Throwable) : AppStateHistory()
    data class EmptyData(val message: String) : AppStateHistory()
    object Loading : AppStateHistory()
}
