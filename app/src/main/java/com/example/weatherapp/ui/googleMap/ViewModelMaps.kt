package com.example.weatherapp.ui.googleMap

import android.location.Address
import android.location.Geocoder
import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.App
import com.example.weatherapp.data.RepositoryImpl
import com.example.weatherapp.data.RetrofitCleint
import com.example.weatherapp.domain.model.Weather
import com.example.weatherapp.domain.model.repository.Repository
import com.example.weatherapp.ui.viewmodel.appSatets.AppStateMap
import com.google.android.gms.maps.model.LatLng
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

private const val SERVER_ERROR = "Ошибка сервера"
private const val CORRUPTED_DATA = "Неполные данные"
private const val REQUEST_ERROR = "Ошибка запроса на сервер"

class ViewModelMaps : ViewModel() {

    private val liveDataToObserve: MutableLiveData<AppStateMap> = MutableLiveData()
    fun getLiveDataLocation(): LiveData<AppStateMap> = liveDataToObserve

    private val repository: Repository = RepositoryImpl(RetrofitCleint())

    private lateinit var addresses: MutableList<Address>
    private var searchTextLoc: String = ""

    private val callBack = object : Callback<Weather> {
        override fun onResponse(call: Call<Weather>, response: Response<Weather>) {

            val weather: Weather? = response.body()
            liveDataToObserve.postValue(
                if (response.isSuccessful && weather != null) {
                    checkResponse(weather)
                } else {
                    AppStateMap.Error(Throwable(SERVER_ERROR))
                })
        }

        override fun onFailure(call: Call<Weather>, t: Throwable) {
            liveDataToObserve.postValue(AppStateMap.Error(Throwable(t.message ?: REQUEST_ERROR)))
        }
    }

    private val callBackClickMap = object : Callback<Weather> {
        override fun onResponse(call: Call<Weather>, response: Response<Weather>) {

            val weather: Weather? = response.body()
            liveDataToObserve.postValue(
                if (response.isSuccessful && weather != null) {
                    checkResponseClickMap(weather)
                } else {
                    AppStateMap.Error(Throwable(SERVER_ERROR))
                })
        }

        override fun onFailure(call: Call<Weather>, t: Throwable) {
            liveDataToObserve.postValue(AppStateMap.Error(Throwable(t.message ?: REQUEST_ERROR)))
        }
    }

    private fun checkResponseClickMap(weather: Weather): AppStateMap {
        return if (weather.current.temp_c != null || weather.current.humidity != null || weather.forecast.forecastday != null) {
            AppStateMap.SuccessClickMap(weather = weather,
                addresses = addresses,
                searchText = searchTextLoc)
        } else {
            AppStateMap.Error(Throwable(CORRUPTED_DATA))
        }
    }

    private fun checkResponse(weather: Weather): AppStateMap {
        return if (weather.current.temp_c != null || weather.current.humidity != null || weather.forecast.forecastday != null) {
            AppStateMap.Success(weather = weather,
                addresses = addresses,
                searchText = searchTextLoc)
        } else {
            AppStateMap.Error(Throwable(CORRUPTED_DATA))
        }
    }

    fun getAddressAsync(location: LatLng) {

        val geoCoder = Geocoder(App.getAppContext())
        Thread {
            try {
                addresses = geoCoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    1
                )

                repository.getWeatherFromServer(addresses.get(0).latitude.toString() + "," + addresses[0].longitude.toString(),
                    callBackClickMap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }

    fun initSearchByAddress(searchText: String) {

        val geoCoder = Geocoder(App.getAppContext())
        searchTextLoc = searchText

        val handle = Handler()
        Thread {
            try {
                addresses = geoCoder.getFromLocationName(searchText, 1)
                if (addresses.size > 0) {

                    repository.getWeatherFromServer(addresses.get(0).latitude.toString() + "," + addresses[0].longitude.toString(),
                        callBack)
                } else {
                    handle.post {
                        liveDataToObserve.value = AppStateMap.Error(Throwable("Error "))
                    }

                }

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }
}