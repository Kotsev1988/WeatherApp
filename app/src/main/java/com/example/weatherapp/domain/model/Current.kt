package com.example.weatherapp.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Current(
    val feelslike_c: Double,
    val humidity: Int,
    val temp_c: Double


) : Parcelable