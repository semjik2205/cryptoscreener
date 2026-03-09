package com.example.cryptoscreener.data.api.dto

import com.google.gson.annotations.SerializedName

data class CryptoDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("symbol")
    val symbol: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("current_price")
    val currentPrice: Double,

    @SerializedName("price_change_percentage_24h")
    val priceChangePercent: Double,

    @SerializedName("image")
    val imageUrl: String
)