package com.example.cryptoscreener.data.api

import com.example.cryptoscreener.data.api.dto.CryptoDto
import com.example.cryptoscreener.data.api.dto.ChartResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Path

interface CoinGeckoApi {

    @GET("coins/markets")
    suspend fun getMarkets(
        @Query("vs_currency") currency: String = "usd",
        @Query("order") order: String = "market_cap_desc",
        @Query("per_page") perPage: Int = 100,
        @Query("page") page: Int = 1,
        @Query("sparkline") sparkline: Boolean = false,
        @Query("price_change_percentage") priceChangePercentage: String = "24h"
    ): List<CryptoDto>
    @GET("coins/{id}/market_chart")
    suspend fun getMarketChart(
        @Path("id") id: String,
        @Query("vs_currency") currency: String = "usd",
        @Query("days") days: String = "1"
    ): ChartResponse
}