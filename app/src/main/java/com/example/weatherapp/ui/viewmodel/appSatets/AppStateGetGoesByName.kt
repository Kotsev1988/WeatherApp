package com.example.weatherapp.ui.viewmodel.appSatets

import android.location.Address

sealed class AppStateGetGoesByName{
    data class Success(val location: MutableList<Address>) : AppStateGetGoesByName()
    data class Error(val error: Throwable) : AppStateGetGoesByName()
}
