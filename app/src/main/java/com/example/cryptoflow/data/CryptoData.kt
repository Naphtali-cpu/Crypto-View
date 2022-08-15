package com.example.cryptoflow.data

data class CryptoData(
    val id: String,
    val symbol: String,
    val name: String,
    val image: String,
    val current_price: Double,
    val price_change_percentage_24h: Double,
    val market_cap: String,
    val last_updated: String,
    val total_supply: String,
    val total_volume: String,
    val market_cap_rank: String,
    val high_24h: Float
)
