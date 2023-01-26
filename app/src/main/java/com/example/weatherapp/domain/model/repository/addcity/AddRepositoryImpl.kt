package com.example.weatherapp.domain.model.repository.addcity

import androidx.lifecycle.LiveData
import com.example.weatherapp.domain.model.Cities
import com.example.weatherapp.room.addingcity.AddCityDao
import com.example.weatherapp.utils.convertAddEntityToCity
import com.example.weatherapp.utils.convertAddEntityToCity1
import com.example.weatherapp.utils.convertCityToEntity

class AddRepositoryImpl(
    private val addRepository: AddCityDao,
) : AddRepository {
    override fun getAll(): List<Cities> {
        return convertAddEntityToCity(addRepository.all())
    }

    override fun getAllLive(): LiveData<List<Cities>> {
        return convertAddEntityToCity1(addRepository.allLiveData())
    }

    override fun getListOfRussianCities(): List<Cities> {
        return convertAddEntityToCity(addRepository.getRussianCities())
    }

    override fun getListOfWorldCities(): List<Cities> {
        return convertAddEntityToCity(addRepository.getWorldCities())
    }

    override fun addCity(cities: Cities) {
        addRepository.insert(convertCityToEntity(cities))
    }
}