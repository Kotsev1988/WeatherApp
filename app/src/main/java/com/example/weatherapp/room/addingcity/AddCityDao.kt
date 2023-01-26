package com.example.weatherapp.room.addingcity

import androidx.lifecycle.LiveData
import androidx.room.*
@Dao
interface AddCityDao {
    @Query("SELECT * FROM AddCityEntities")
    fun all(): List<AddCityEntities>

    @Query("SELECT * FROM AddCityEntities")
    fun allLiveData(): LiveData<List<AddCityEntities>>

    @Query("SELECT * FROM AddCityEntities WHERE city LIKE :city")
    fun getDataByWord(city: String): List<AddCityEntities>

    @Query("SELECT * FROM AddCityEntities WHERE isRus ="+true)
    fun getRussianCities(): List<AddCityEntities>

    @Query("SELECT * FROM AddCityEntities WHERE isRus ="+false)
    fun getWorldCities(): List<AddCityEntities>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: AddCityEntities)

    @Update
    fun update(entity: AddCityEntities)

    @Delete
    fun delete(entity: AddCityEntities)
}