package com.example.weatherapp.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Weather(

    val current: Current,
    val forecast: Forecast,
    val location: Location
) : Parcelable

