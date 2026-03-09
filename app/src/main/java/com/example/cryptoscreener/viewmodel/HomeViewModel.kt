package com.example.cryptoscreener.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoscreener.data.repository.CryptoRepository
import com.example.cryptoscreener.model.Crypto
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val repository = CryptoRepository

    private val _topGainers = MutableLiveData<List<Crypto>>()
    val topGainers: LiveData<List<Crypto>> = _topGainers

    private val _topLosers = MutableLiveData<List<Crypto>>()
    val topLosers: LiveData<List<Crypto>> = _topLosers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadData() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                // Загружаем данные один раз и делим на gainers/losers
                val cryptos = repository.getTopCryptos()
                _topGainers.value = cryptos
                    .sortedByDescending { it.priceChangePercent }
                    .take(3)
                _topLosers.value = cryptos
                    .sortedBy { it.priceChangePercent }
                    .take(3)

            } catch (e: Exception) {
                _error.value = "Ошибка загрузки: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}