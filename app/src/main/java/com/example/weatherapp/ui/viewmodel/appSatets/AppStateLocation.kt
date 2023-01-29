package com.example.weatherapp.ui.viewmodel.appSatets

import com.example.weatherapp.domain.model.Weather

sealed class AppStateLocation{
    data class Success(val weather: Weather?, val cities: String) : AppStateLocation()
    data class Error(val error: Throwable) : AppStateLocation()
    data class EmptyData(val message: String) : AppStateLocation()
    data class ShowRationalDialog(val show: String) : AppStateLocation()
}
