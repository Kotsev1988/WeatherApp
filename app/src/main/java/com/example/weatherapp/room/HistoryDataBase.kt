package com.example.weatherapp.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weatherapp.room.addingcity.AddCityDao
import com.example.weatherapp.room.addingcity.AddCityEntities


@Database(entities = [HistoryEntity::class, AddCityEntities::class], version = 1, exportSchema =
false)
abstract class HistoryDataBase  : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
    abstract fun citiesDao(): AddCityDao
}