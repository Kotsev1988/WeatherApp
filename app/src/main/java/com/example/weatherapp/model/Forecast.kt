package com.example.weatherapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Forecast(
    val forecastday: List<Forecastday>
): Parcelable