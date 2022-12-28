package com.example.weatherapp.data

import android.app.IntentService
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.domain.model.Current
import com.example.weatherapp.domain.model.Forecast
import com.example.weatherapp.domain.model.Location
import com.example.weatherapp.domain.model.Weather
import com.example.weatherapp.ui.*
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors

const val CITY_NAME_EXTRA = "CITY_NAME_EXTRA"

class WeatherService(name: String = "WeatherService") : IntentService(name) {

    private val broadcastIntent = Intent(INTENT_FILTER)

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onHandleIntent(intent: Intent?) {
        if (intent == null) {

            onEmptyResponse()

        } else {
            val cityName = intent.getStringExtra(CITY_NAME_EXTRA)
            if (cityName == "") {
                onEmptyData()
            } else {
                onResponseSuccess(cityName)
            }
        }
    }

    private fun onEmptyData() {

        broadcastIntent.putExtra(DETAILS_LOAD_RESULT_EXTRA, DETAILS_DATA_EMPTY_EXTRA)
        broadcastIntent.putExtra(EMPTY_DATA_MESSAGE, "Empty Data")
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)

    }

    private fun onEmptyResponse() {

        broadcastIntent.putExtra(DETAILS_LOAD_RESULT_EXTRA, DETAILS_RESPONSE_EMPTY_EXTRA)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun onResponseSuccess(cityName: String?) {

        val uri =
            URL("http://api.weatherapi.com/v1/forecast.json?key=${BuildConfig.APP_KEY}&q=${cityName}&days=1&aqi=no&alerts=no&lang=ru")

        try {
            Thread {
                lateinit var urlConnection: HttpURLConnection

                try {
                    urlConnection = (uri.openConnection() as HttpURLConnection).apply {
                        requestMethod = "GET"
                        readTimeout = 10000
                    }

                    val bufferedReader =
                        BufferedReader(InputStreamReader(urlConnection.inputStream))
                    val response = getLines(bufferedReader)

                    val weather: Weather = Gson().fromJson(response, Weather::class.java)
                    onResponse(weather)

                } catch (e: Exception) {
                    onResponseFailed(e.message ?: "Empty error")
                    e.printStackTrace()
                } finally {
                    urlConnection.disconnect()
                }
            }.start()
        } catch (e: MalformedURLException) {
            e.printStackTrace()
            onMalFormedException()
        }
    }

    private fun onResponse(weather: Weather) {
        if (weather == null) {
            onEmptyData()
        } else {
            onSuccess(weather.current.temp_c,
                weather.current.feelslike_c,
                weather.current.humidity,
                weather.forecast,
                weather.location.name)
        }
    }

    private fun onSuccess(
        tempC: Double,
        feelLike: Double,
        humidity: Int,
        forecast: Forecast,
        location: String,
    ) {

        broadcastIntent.putExtra(DETAILS_LOAD_RESULT_EXTRA, DETAILS_RESPONSE_SUCCESS_EXTRA)
        broadcastIntent.putExtra(DETAILS_TEMP_EXTRA, tempC)
        broadcastIntent.putExtra(FEELS_LIKE_EXTRA, feelLike)
        broadcastIntent.putExtra(HUMIDITY_DETAILS_EXTRA, humidity)
        val forecastConvert = Gson().toJson(forecast)
        broadcastIntent.putExtra(DETAILS_FORECAST_EXTRA, forecastConvert)
        broadcastIntent.putExtra(DETAILS_LOCATION_EXTRA, location)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)

    }

    private fun onResponseFailed(s: String) {

        broadcastIntent.putExtra(DETAILS_LOAD_RESULT_EXTRA, DETAILS_REQUEST_ERROR_EXTRA)
        broadcastIntent.putExtra(DETAILS_REQUEST_ERROR_MESSAGE_EXTRA, s)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)

    }

    private fun onMalFormedException() {

        broadcastIntent.putExtra(DETAILS_LOAD_RESULT_EXTRA, DETAILS_URL_MALFORMED_EXTRA)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLines(reader: BufferedReader): String {
        return reader.lines().collect(Collectors.joining("\n"))
    }

}