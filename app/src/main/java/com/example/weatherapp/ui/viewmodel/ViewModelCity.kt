package com.example.weatherapp.ui.viewmodel

import android.content.Context
import android.location.Geocoder
import androidx.lifecycle.*
import com.example.weatherapp.App
import com.example.weatherapp.data.RepositoryImpl
import com.example.weatherapp.data.RetrofitCleint
import com.example.weatherapp.domain.model.repository.Repository
import com.example.weatherapp.domain.model.Weather
import com.example.weatherapp.domain.model.repository.history.LocalRepository
import com.example.weatherapp.domain.model.repository.history.LocalRepositoryImpl
import com.example.weatherapp.ui.viewmodel.appSatets.AppState
import com.example.weatherapp.ui.viewmodel.appSatets.AppStateForCity
import com.example.weatherapp.ui.viewmodel.appSatets.AppStateGetGoesByName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val REQUEST_ERROR = "Ошибка запроса на сервер"
private const val SERVER_ERROR = "Ошибка сервера"
private const val CORRUPTED_DATA = "Неполные данные"
private const val REQUEST_ERROR_LOCATION = "Ошибка запроса локации"

class ViewModelCity(
    val detailsLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val repository: Repository = RepositoryImpl(RetrofitCleint()),
    private val historyRepository: LocalRepository = LocalRepositoryImpl(App.getHistoryDao()),
) : ViewModel() {

    private val liveDataToObserve: MutableLiveData<AppStateForCity> = MutableLiveData()
    fun getLiveData(): LiveData<AppStateForCity> = liveDataToObserve

    private val liveDataLocation: MutableLiveData<AppStateGetGoesByName> = MutableLiveData()
    fun getLiveDataLocation(): LiveData<AppStateGetGoesByName> = liveDataLocation


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
        return if (weather.current.temp_c != null || weather.current.humidity != null
            || weather.forecast.forecastday != null
        ) {
            AppStateForCity.Success(weather = weather)
        } else {
            AppStateForCity.Error(Throwable(CORRUPTED_DATA))
        }
    }

    fun getWeather(city: String) = getDataFromService(city)

    private fun getDataFromService(city: String) {

        detailsLiveData.value = AppState.Loading
        repository.getWeatherFromServer(city, callBack)
    }

    fun getAddressAsync(context: Context, location: String) {

        val geoCoder = Geocoder(context)

        viewModelScope.launch {
            val addresses = geoCoder.getFromLocationName(location, 1)
            if (addresses != null) {
                withContext(Dispatchers.Main) {

                    liveDataLocation.value = AppStateGetGoesByName.Success(addresses)
                }
            } else {
                withContext(Dispatchers.Main) {

                    liveDataLocation.value =
                        AppStateGetGoesByName.Error(Throwable(REQUEST_ERROR_LOCATION))
                }
            }
        }
    }

    fun saveCityToDB(weather: Weather) {
        historyRepository.saveEntity(weather)
    }
}