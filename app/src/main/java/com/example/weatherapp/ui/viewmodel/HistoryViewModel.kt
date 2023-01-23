package com.example.weatherapp.ui.viewmodel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.App
import com.example.weatherapp.domain.model.repository.history.LocalRepository
import com.example.weatherapp.domain.model.repository.history.LocalRepositoryImpl

class HistoryViewModel(
    val historyLiveData: MutableLiveData<AppStateHistory> = MutableLiveData(),
    private val historyRepository: LocalRepository = LocalRepositoryImpl(App.getHistoryDao())
): ViewModel() {

    fun getAllHistory(){
        historyLiveData.value = AppStateHistory.Loading
        historyLiveData.value = AppStateHistory.Success(historyRepository.getAllHistory())
    }

}