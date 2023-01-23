package com.example.weatherapp.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var city: String,
    var humidity: Int,
    var feelslike_c: Double,
    var temp_c:  Double
)
