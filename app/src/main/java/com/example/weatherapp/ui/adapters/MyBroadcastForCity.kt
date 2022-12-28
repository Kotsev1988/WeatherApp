package com.example.weatherapp.ui.adapters

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.LiveData
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.weatherapp.domain.model.Current
import com.example.weatherapp.domain.model.Forecast
import com.example.weatherapp.domain.model.Location
import com.example.weatherapp.domain.model.Weather
import com.example.weatherapp.ui.*
import com.example.weatherapp.ui.viewmodel.AppStateForCity
import com.google.gson.Gson

private const val PROCESS_ERROR = "Обработка ошибки"
const val INTENT_FILTER = "INTENT FILTER"
const val DETAILS_TEMP_EXTRA = "DETAILS_TEMP_EXTRA"
const val DETAILS_RESPONSE_SUCCESS_EXTRA = "RESPONSE SUCCESS"
const val DETAILS_LOAD_RESULT_EXTRA = "LOAD RESULT"
const val DETAILS_DATA_EMPTY_EXTRA = "DETAILS_DATA_EMPTY_EXTRA"
const val DETAILS_RESPONSE_EMPTY_EXTRA = "RESPONSE IS EMPTY"
const val DETAILS_URL_MALFORMED_EXTRA = "URL MALFORMED"
const val DETAILS_REQUEST_ERROR_MESSAGE_EXTRA = "DETAILS_REQUEST_ERROR_MESSAGE_EXTRA"
const val DETAILS_LOCATION_EXTRA = "DETAILS_LOCATION_EXTRA"
const val DETAILS_FORECAST_EXTRA = "DETAILS_FORECAST_EXTRA"
const val DETAILS_REQUEST_ERROR_EXTRA = "DETAILS_REQUEST_ERROR_EXTRA"
const val FEELS_LIKE_EXTRA = "FEELS_LIKE_EXTRA"
const val HUMIDITY_DETAILS_EXTRA = "HUMIDITY_DETAILS_EXTRA"
const val EMPTY_DATA_MESSAGE = "EMPTY_DATA_MESSAGE"

class MyBroadcastForCity(val context: Context) : LiveData<AppStateForCity>() {

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.getStringExtra(DETAILS_LOAD_RESULT_EXTRA)) {

                DETAILS_RESPONSE_SUCCESS_EXTRA -> {

                    val current = Current(
                        intent.getDoubleExtra(FEELS_LIKE_EXTRA, 0.0),
                        intent.getIntExtra(HUMIDITY_DETAILS_EXTRA, 0),
                        intent.getDoubleExtra(DETAILS_TEMP_EXTRA, 0.0))

                    val forecast = intent.getStringExtra(DETAILS_FORECAST_EXTRA)
                    val location = intent.getStringExtra(DETAILS_LOCATION_EXTRA)
                    if (forecast != null) {

                        location?.let { Location(it) }?.let {
                            value = AppStateForCity.Success(Weather(
                                current,
                                Gson().fromJson(forecast, Forecast::class.java),
                                it
                            ))
                        }
                    }
                }

                DETAILS_DATA_EMPTY_EXTRA -> {

                    val empty = intent.getStringExtra(EMPTY_DATA_MESSAGE)
                    value = empty?.let { AppStateForCity.EmptyData(it) }
                }
                DETAILS_RESPONSE_EMPTY_EXTRA -> {
                    TODO(PROCESS_ERROR)
                }
                DETAILS_URL_MALFORMED_EXTRA -> {
                    TODO(PROCESS_ERROR)
                }
                DETAILS_REQUEST_ERROR_EXTRA -> {

                    val error = intent.getStringExtra(DETAILS_REQUEST_ERROR_MESSAGE_EXTRA)
                    value = error?.let { AppStateForCity.Error(it) }

                }
            }
        }
    }


    override fun onActive() {
        super.onActive()

        LocalBroadcastManager.getInstance(context)
            .registerReceiver(broadcastReceiver,
                IntentFilter(INTENT_FILTER))

    }

    override fun onInactive() {
        super.onInactive()

        LocalBroadcastManager.getInstance(context)
            .registerReceiver(broadcastReceiver,
                IntentFilter(INTENT_FILTER))
    }
}