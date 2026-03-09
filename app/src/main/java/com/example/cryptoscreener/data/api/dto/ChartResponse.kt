package com.example.cryptoscreener.data.api.dto

import com.google.gson.annotations.SerializedName

data class ChartResponse(
    @SerializedName("prices")
    val prices: List<List<Double>>
)