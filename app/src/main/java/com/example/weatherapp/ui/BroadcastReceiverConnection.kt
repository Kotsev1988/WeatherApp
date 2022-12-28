package com.example.weatherapp.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class BroadcastReceiverConnection() : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        println("Connection is changed")
        Toast.makeText(context, "Connection is changed", Toast.LENGTH_SHORT).show()
    }
}