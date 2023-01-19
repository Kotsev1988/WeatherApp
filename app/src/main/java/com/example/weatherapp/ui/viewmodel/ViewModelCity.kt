package com.example.weatherapp.ui.viewmodel

import androidx.lifecycle.*
import com.example.weatherapp.data.RepositoryImpl
import com.example.weatherapp.data.RetrofitCleint
import com.example.weatherapp.domain.Repository
import com.example.weatherapp.domain.model.Weather
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val REQUEST_ERROR = "Ошибка запроса на сервер"
private const val SERVER_ERROR = "Ошибка сервера"
private const val CORRUPTED_DATA = "Неполные данные"

class ViewModelCity() : ViewModel() {

    private val repository: Repository = RepositoryImpl(RetrofitCleint())
    private val liveDataToObserve: MutableLiveData<AppStateForCity> = MutableLiveData()

    fun getLiveData(): LiveData<AppStateForCity> = liveDataToObserve

    private val callBack = object : Callback<Weather> {
        override fun onResponse(call: Call<Weather>, response: Response<Weather>) {

            val weather: Weather? = response.body()
            liveDataToObserve.postValue(if (response.isSuccessful && weather != null) {
                checkResponse(weather)
            } else {
                AppStateForCity.Error(Throwable(SERVER_ERROR))
            })
        }

        override fun onFailure(call: Call<Weather>, t: Throwable) {
            liveDataToObserve.postValue(AppStateForCity.Error(Throwable(t.message
                ?: REQUEST_ERROR)))
        }
    }

    private fun checkResponse(weather: Weather): AppStateForCity {
        return if (weather.current.temp_c != null || weather.current.humidity != null || weather.forecast.forecastday != null) {
            AppStateForCity.Success(weather = weather)
        } else {
            AppStateForCity.Error(Throwable(CORRUPTED_DATA))
        }
    }

    suspend fun getWeather(city: String) = getDataFromService(city)

    suspend fun getDataFromService(city: String) {

        repository.getWeatherFromServer(city, callBack)
    }


}