package com.example.weatherapp.ui.viewmodel


import android.os.Handler
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.App
import com.example.weatherapp.domain.model.repository.history.LocalRepository
import com.example.weatherapp.domain.model.repository.history.LocalRepositoryImpl
import com.example.weatherapp.ui.viewmodel.appSatets.AppStateHistory

class HistoryViewModel(
    val historyLiveData: MutableLiveData<AppStateHistory> = MutableLiveData(),
    private val historyRepository: LocalRepository = LocalRepositoryImpl(App.getHistoryDao())
): ViewModel() {

    fun getAllHistory(){
        val handler = Handler()
        Thread{
            val listOfWeather = historyRepository.getAllHistory()

            handler.post{
                historyLiveData.value = AppStateHistory.Loading
                historyLiveData.value = AppStateHistory.Success(listOfWeather)
            }
        }.start()
    }

    fun getFilterData(newText: String) {
        val handler = Handler()
        Thread{
            val filter = historyRepository.getFilterData(newText)
            handler.post {
                historyLiveData.value = AppStateHistory.Success(filter)
            }
        }.start()
    }

}