package com.example.weatherapp.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Cities(val cityName: String, val  isRus: Boolean) : Parcelable



fun getRussianCities(): List<Cities> {
    return listOf(
        Cities("Москва",true),
        Cities("Баксан", true),
        Cities("Волгоград",true),
        Cities("Владивосток", true),
        Cities("Челябинск",true)
    )
}

fun getWorldCities(): List<Cities> {
    return listOf(
        Cities("Лондон", false),
        Cities("Париж",false),
        Cities("Питтсбург", false),
        Cities("Милан", false),
        Cities("Рим",false)
    )
}


