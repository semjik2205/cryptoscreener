package com.example.cryptoscreener.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoscreener.data.repository.CryptoRepository
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {

    private val repository = CryptoRepository

    private val _chartData = MutableLiveData<List<Pair<Long, Double>>>()
    val chartData: LiveData<List<Pair<Long, Double>>> = _chartData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    var selectedDays = "1"

    fun loadChart(coinId: String, days: String = selectedDays) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                selectedDays = days
                val data = repository.getChartData(coinId, days)
                _chartData.value = data
            } catch (e: Exception) {
                _error.value = "Ошибка загрузки: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}