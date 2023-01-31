package com.example.weatherapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.weatherapp.ui.INTENT_FILTER
import com.example.weatherapp.ui.PUT_CITY_EXTRA

class MyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val message = intent.getStringExtra(PUT_CITY_EXTRA)

        val intent1 = Intent(INTENT_FILTER)
        intent1.putExtra(PUT_CITY_EXTRA , message)
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent1)

    }
}