package com.example.weatherapp.room.addingcity

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class AddCityEntities (

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var city: String,
    var isRus: Boolean

)