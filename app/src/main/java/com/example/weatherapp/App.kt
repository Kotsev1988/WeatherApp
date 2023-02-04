package com.example.weatherapp

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.weatherapp.room.HistoryDao
import com.example.weatherapp.room.HistoryDataBase
import com.example.weatherapp.room.addingcity.AddCityDao

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appInstance = this

    }

    companion object {
        private var appInstance: App? = null
        private var db: HistoryDataBase? = null
        private const val DB_NAME = "History.db"
        fun getHistoryDao(): HistoryDao {
            if (db == null) {
                synchronized(HistoryDataBase::class.java) {
                    if (db == null) {
                        if (appInstance == null) throw
                        IllegalStateException("Application is null while creating DataBase")

                        db = Room.databaseBuilder(
                            appInstance!!.applicationContext,
                            HistoryDataBase::class.java,
                            DB_NAME)
                            .build()
                    }
                }
            }
            return db!!.historyDao()
        }

        fun getCitiesDao(): AddCityDao {

            if (db == null) {
                synchronized(HistoryDataBase::class.java) {
                    if (db == null) {
                        if (appInstance == null) throw
                        IllegalStateException("Application is null while creating DataBase")
                        db = Room.databaseBuilder(
                            appInstance!!.applicationContext,
                            HistoryDataBase::class.java,
                            DB_NAME)
                            .build()
                    }
                }
            }
            return db!!.citiesDao()
        }

        fun getAppContext(): Context {
            return appInstance!!.applicationContext
        }
    }

}