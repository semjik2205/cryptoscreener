package com.example.cryptoscreener.data.repository

import com.example.cryptoscreener.data.api.RetrofitClient
import com.example.cryptoscreener.model.Crypto

class CryptoRepository {

    private val api = RetrofitClient.coinGeckoApi

    suspend fun getTopCryptos(): List<Crypto> {
        return api.getMarkets().map { dto ->
            Crypto(
                id = dto.id,
                name = dto.name,
                symbol = dto.symbol,
                currentPrice = dto.currentPrice,
                priceChangePercent = dto.priceChangePercent,
                imageUrl = dto.imageUrl
            )
        }
    }
}