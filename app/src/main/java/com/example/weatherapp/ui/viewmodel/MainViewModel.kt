package com.example.weatherapp.ui.viewmodel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import com.example.weatherapp.App
import com.example.weatherapp.data.*
import com.example.weatherapp.domain.model.repository.Repository
import com.example.weatherapp.domain.model.*
import com.example.weatherapp.domain.model.repository.addcity.AddRepository
import com.example.weatherapp.domain.model.repository.addcity.AddRepositoryImpl
import com.example.weatherapp.ui.viewmodel.appSatets.AppState
import com.example.weatherapp.ui.viewmodel.appSatets.AppStateLocation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

private const val REQUEST_ERROR = "Ошибка запроса на сервер"
private const val SERVER_ERROR = "Ошибка сервера"
private const val CORRUPTED_DATA = "Неполные данные"

private const val REFRESH_PERIOD = 60000L
private const val MINIMAL_DISTANCE = 100f

class MainViewModel(
    private val addRepository: AddRepository = AddRepositoryImpl(App.getCitiesDao())
) : ViewModel() {

    private val repository: Repository = RepositoryImpl(RetrofitCleint())

    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()
    fun getLiveData(): LiveData<AppState> = liveDataToObserve

    private val liveDataListCities: MutableLiveData<List<Cities>> = MutableLiveData()
    fun getLiveDataCities(): LiveData<List<Cities>> = liveDataListCities

    private val liveDataLocation: MutableLiveData<AppStateLocation> = MutableLiveData()
    fun getLiveDataLocation(): LiveData<AppStateLocation> = liveDataLocation

    private var citiesList: List<Cities> = listOf()
    private var city: String= ""

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

    private val callBackLocation = object : Callback<Weather> {
        override fun onResponse(call: Call<Weather>, response: Response<Weather>) {

            val weather: Weather? = response.body()
            liveDataLocation.postValue(if (response.isSuccessful && weather != null) {

                checkResponseLocation(weather, city)
            } else {
                AppStateLocation.Error(Throwable(SERVER_ERROR))
            })
        }

        override fun onFailure(call: Call<Weather>, t: Throwable) {
           liveDataLocation.postValue(AppStateLocation.Error(Throwable(t.message ?: REQUEST_ERROR)))
        }
    }

    private val onLocationListener = object : LocationListener{
        override fun onLocationChanged(location: android.location.Location) {
            App.getAppContext()?.let {
                getAddressAsync(it, location)
            }
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            super.onStatusChanged(provider, status, extras)
        }

        override fun onProviderDisabled(provider: String) {
            super.onProviderDisabled(provider)
        }

        override fun onProviderEnabled(provider: String) {
            super.onProviderEnabled(provider)
        }
    }

    private fun checkResponse(weather: Weather): AppState {
        return if (weather.current.temp_c != null || weather.current.humidity != null
            || weather.forecast.forecastday != null) {
            AppState.Success(weather = weather, citiesList)
        } else {
            AppState.Error(Throwable(CORRUPTED_DATA))
        }
    }

    private fun checkResponseLocation(weather: Weather, city: String): AppStateLocation {
        return if (weather.current.temp_c != null || weather.current.humidity != null
            || weather.forecast.forecastday != null) {
            AppStateLocation.Success(weather = weather, city)
        } else {
            AppStateLocation.Error(Throwable(CORRUPTED_DATA))
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

     fun getLocation(){

        App.getAppContext()?.let { context ->
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED){

                val locationManager = context.getSystemService(Context.LOCATION_SERVICE)
                        as LocationManager
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    val provider = locationManager.getProvider(LocationManager.GPS_PROVIDER)
                    provider?.let {
                        locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            REFRESH_PERIOD,
                            MINIMAL_DISTANCE,
                            onLocationListener
                        )
                    }
                } else {
                    val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    if (location == null){
                      liveDataLocation.value= AppStateLocation.EmptyData("Empty")
                    } else {
                        getAddressAsync(context, location)
                    }
                }
            } else {
                liveDataLocation.value= AppStateLocation.ShowRationalDialog("show")
            }
        }
    }

    private fun getAddressAsync(context: Context, location: android.location.Location) {

        val geoCoder = Geocoder(context)
        Thread{
            try {
                val addresses = geoCoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    1
                )

                city = addresses[0].getAddressLine(0)
                repository.getWeatherFromServer(city, callBackLocation)

            }catch (e: IOException){
                e.printStackTrace()
            }
        }.start()
    }
}



