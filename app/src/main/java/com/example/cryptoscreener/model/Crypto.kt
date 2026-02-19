package com.example.cryptoscreener.model

data class Crypto(
    val id: String,
    val name: String,
    val symbol: String,
    val currentPrice: Double,
    val priceChangePercent: Double,
    val imageUrl: String
)