package com.example.weatherapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Cities(val cityName: String):Parcelable

fun getRussianCities(): List<Cities> {
    return listOf(
        Cities("Москва"),
        Cities("Баксан"),
        Cities("Волгоград"),
        Cities("Владивосток"),
        Cities("Челябинск")
    )
}

fun getWorldCities(): List<Cities> {
    return listOf(
        Cities("Лондон"),
        Cities("Париж"),
        Cities("Питтсбург"),
        Cities("Милан"),
        Cities("Рим")
    )
}
