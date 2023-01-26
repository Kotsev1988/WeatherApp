package com.example.weatherapp.ui.viewmodel

import androidx.lifecycle.*
import com.example.weatherapp.App
import com.example.weatherapp.data.*
import com.example.weatherapp.domain.model.repository.Repository
import com.example.weatherapp.domain.model.*
import com.example.weatherapp.domain.model.repository.addcity.AddRepository
import com.example.weatherapp.domain.model.repository.addcity.AddRepositoryImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val REQUEST_ERROR = "Ошибка запроса на сервер"
private const val SERVER_ERROR = "Ошибка сервера"
private const val CORRUPTED_DATA = "Неполные данные"

class MainViewModel(
    private val addRepository: AddRepository = AddRepositoryImpl(App.getCitiesDao())
) : ViewModel() {

    private val repository: Repository = RepositoryImpl(RetrofitCleint())

    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()
    fun getLiveData(): LiveData<AppState> = liveDataToObserve

    private val liveDataListCities: MutableLiveData<List<Cities>> = MutableLiveData()
    fun getLiveDataCities(): LiveData<List<Cities>> = liveDataListCities

    private var citiesList: List<Cities> = listOf()

    private val callBack = object : Callback<Weather> {
        override fun onResponse(call: Call<Weather>, response: Response<Weather>) {

            val weather: Weather? = response.body()
            liveDataToObserve.postValue(if (response.isSuccessful && weather != null) {
                checkResponse(weather)
            } else {
                AppState.Error(Throwable(SERVER_ERROR))
            })
        }

        override fun onFailure(call: Call<Weather>, t: Throwable) {
            liveDataToObserve.postValue(AppState.Error(Throwable(t.message ?: REQUEST_ERROR)))
        }
    }

    private fun checkResponse(weather: Weather): AppState {
        return if (weather.current.temp_c != null || weather.current.humidity != null || weather.forecast.forecastday != null) {
            AppState.Success(weather = weather, citiesList)
        } else {
            AppState.Error(Throwable(CORRUPTED_DATA))
        }
    }

     fun getWeather(isRus: Boolean) = getDataFromService(isRus)

     fun getDataFromService(isRus: Boolean) {

         if (addRepository.getAll().isEmpty()){
             repository.getListOfRussianCities().forEach {
                 addRepository.addCity(it)
             }

             repository.getListOfWorldCities().forEach {
                 addRepository.addCity(it)
             }

         }

        citiesList = when (isRus) {
            true -> {
                addRepository.getListOfRussianCities()

            }
            false -> {
                addRepository.getListOfWorldCities()
            }
        }
         liveDataListCities.value = citiesList
        repository.getWeatherFromServer(citiesList[0].cityName, callBack)
    }

    fun updateCitites(){

            addRepository.getListOfRussianCities1().observeForever {

                liveDataListCities.value = it
            }

            addRepository.getListOfWorldCities1().observeForever {

                liveDataListCities.value = it
            }
    }
}



