package com.example.weatherapp.ui.viewmodel.appSatets

import android.location.Address
import com.example.weatherapp.domain.model.Weather

sealed class AppStateMap{
    data class Success(val addresses: MutableList<Address>, val weather: Weather?, val searchText: String) : AppStateMap()
    data class SuccessClickMap(val addresses: MutableList<Address>, val weather: Weather?, val searchText: String) : AppStateMap()
    data class Error(val error: Throwable) : AppStateMap()
    data class EmptyData(val message: String) : AppStateMap()
}
