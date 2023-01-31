package com.example.weatherapp

import android.annotation.SuppressLint
import android.app.*
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.weatherapp.ui.*
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFireBaseMessaging : FirebaseMessagingService() {

    companion object {
        private const val PUSH_KEY_TITLE = "title"
        private const val PUSH_KEY_MESSAGE = "message"
        private const val CHANNEL_ID = "channel_id"
        private const val NOTIFICATION_ID = 1
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.v("MyFireBaseMessaging", "onNewToken $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        println("ONMESSAGERECEIVE "+message.data)
        val remoteData = message.data

        if (remoteData.isNotEmpty()) {
            handleDataMessage(remoteData.toMap())
        }
    }

    private fun handleDataMessage(data: Map<String, String>) {

        val title = data[PUSH_KEY_TITLE]
        val message = data[PUSH_KEY_MESSAGE]

        if (!title.isNullOrBlank() && !message.isNullOrBlank())
            showNotification(title, message)

    }


    private fun showNotification(title: String, message: String?) {
//        Открытие страницы детализации через stackBuilder

//        var intent= Intent(this, MainActivity::class.java)
//        intent.putExtra("city", title)
//        var stackBuilder = TaskStackBuilder.create(this)
//        stackBuilder.addNextIntent(intent)
//
//        var pendingIntent = stackBuilder.getPendingIntent(0,
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
//
//        val notificationBuilder = NotificationCompat.Builder(applicationContext, CHANNEL_ID).apply {
//            setSmallIcon(R.drawable.ic_notification_foreground)
//            setContentTitle(title)
//            setContentText(message)
//            setContentIntent(pendingIntent)
//            priority = NotificationCompat.PRIORITY_DEFAULT
//        }

        //        Открытие страницы детализации через Broadcast

        var intent= Intent(applicationContext, MyReceiver::class.java)
        intent.putExtra(PUT_CITY_EXTRA , title)

        var pendingIntent = PendingIntent.getBroadcast(applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val notificationBuilder = NotificationCompat.Builder(applicationContext, CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_notification_foreground)
            setContentTitle(title)
            setContentText(message)
            addAction(0, "See more $title", pendingIntent)
            priority = NotificationCompat.PRIORITY_DEFAULT
        }


        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {

        val name = "Channel name"
        val descriptionText = "Channel desc"
        val importance = NotificationManager.IMPORTANCE_DEFAULT

        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }

        notificationManager.createNotificationChannel(channel)
    }
}