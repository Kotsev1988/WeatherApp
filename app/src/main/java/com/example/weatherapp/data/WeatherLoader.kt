package com.example.weatherapp.data

import android.os.Build
import android.os.Handler
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.domain.model.Weather
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors

class WeatherLoader(private val listener: WeatherLoadListener, private val city: String) {

    @RequiresApi(Build.VERSION_CODES.N)
    fun loadWeather(){

        val uri = URL("http://api.weatherapi.com/v1/forecast.json?key=" +
                "${BuildConfig.WEATHER_API_KEY}&q=${city}&days=1&aqi=no&alerts=no&lang=ru")
        val handler = Handler()

        try {
            Thread {
                lateinit var urlConnection : HttpURLConnection

                try {
                    urlConnection = (uri.openConnection() as HttpURLConnection).apply {
                        requestMethod = "GET"
                        readTimeout = 10000
                    }

                    val bufferedReader =
                        BufferedReader(InputStreamReader(urlConnection.inputStream))
                    val response = getLines(bufferedReader)

                    val weather: Weather = Gson().fromJson(response, Weather::class.java)
                    handler.post{
                        listener.onSuccess(weather)
                    }

                }catch (e: Exception){
                    Log.d("", "Fail connection", e)
                    e.printStackTrace()
                    handler.post{
                        listener.onFailed(e)
                    }
                }finally {
                    urlConnection.disconnect()
                }
            }.start()
        }catch (e: MalformedURLException){
            Log.d("", "Fail URl", e)
            e.printStackTrace()
            listener.onFailed(e)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLines(reader: BufferedReader): String{
        return reader.lines().collect(Collectors.joining("\n"))
    }
}