package com.example.cryptoflow.data

data class CryptoData(
    val id: String,
    val symbol: String,
    val name: String,
    val image: String,
    val current_price: String,
    val price_change_percentage_24h: String,
    val price_date: String,
)
