package com.example.cryptoscreener.data.repository

import com.example.cryptoscreener.data.api.RetrofitClient
import com.example.cryptoscreener.model.Crypto

object CryptoRepository {

    private val api = RetrofitClient.coinGeckoApi

    // Кеш данных в памяти
    private var cachedCryptos: List<Crypto> = emptyList()
    private var lastFetchTime: Long = 0
    private val cacheTimeout = 5 * 60 * 1000L // 5 минут

    private suspend fun fetchIfNeeded(): List<Crypto> {
        val now = System.currentTimeMillis()
        if (cachedCryptos.isEmpty() || now - lastFetchTime > cacheTimeout) {
            cachedCryptos = api.getMarkets().map { dto ->
                Crypto(
                    id = dto.id,
                    name = dto.name,
                    symbol = dto.symbol,
                    currentPrice = dto.currentPrice,
                    priceChangePercent = dto.priceChangePercent,
                    imageUrl = dto.imageUrl
                )
            }.filter { crypto ->
                // Убираем стейблкоины — цена около 1$ И минимальное изменение
                val isStablecoin = crypto.currentPrice in 0.90..1.10 &&
                        kotlin.math.abs(crypto.priceChangePercent) < 0.5
                !isStablecoin
            }
            lastFetchTime = now
        }
        return cachedCryptos
    }

    suspend fun getTopCryptos(): List<Crypto> {
        return fetchIfNeeded()
    }

    suspend fun getTopGainers(): List<Crypto> {
        return fetchIfNeeded()
            .sortedByDescending { it.priceChangePercent }
            .take(3)
    }

    suspend fun getTopLosers(): List<Crypto> {
        return fetchIfNeeded()
            .sortedBy { it.priceChangePercent }
            .take(3)
    }
    private val chartCache = mutableMapOf<String, List<Pair<Long, Double>>>()
    private val chartCacheTime = mutableMapOf<String, Long>()
    private val chartCacheTimeout = 10 * 60 * 1000L // 10 минут

    suspend fun getChartData(coinId: String, days: String): List<Pair<Long, Double>> {
        val cacheKey = "${coinId}_${days}"
        val now = System.currentTimeMillis()
        val lastFetch = chartCacheTime[cacheKey] ?: 0L

        if (chartCache.containsKey(cacheKey) && now - lastFetch < chartCacheTimeout) {
            return chartCache[cacheKey]!!
        }

        val response = api.getMarketChart(coinId, days = days)
        val data = response.prices.map { point ->
            Pair(point[0].toLong(), point[1])
        }
        chartCache[cacheKey] = data
        chartCacheTime[cacheKey] = now
        return data
    }
}