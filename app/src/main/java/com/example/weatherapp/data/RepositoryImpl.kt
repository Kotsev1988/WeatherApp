package com.example.weatherapp.data

import com.example.weatherapp.domain.Repository
import com.example.weatherapp.domain.model.Cities
import com.example.weatherapp.domain.model.Weather
import com.example.weatherapp.domain.model.getRussianCities
import com.example.weatherapp.domain.model.getWorldCities
import retrofit2.Response

class RepositoryImpl : Repository {
    private val service = RetrofitCleint.getInstance()
    override suspend fun getWeatherFromServer(city: String) : Response<Weather> =
        service.getWeather(city = city)
    
    override fun getWeatherFromLoader(listener: WeatherLoadListener, city: String): WeatherLoader =
        WeatherLoader(listener, city = city)

    override fun getWeatherFromLocal() : Weather {
        TODO("Not yet implemented")
    }

    override fun getListOfRussianCities() : List<Cities> = getRussianCities()

    override fun getListOfWorldCities() : List<Cities> = getWorldCities()

}
