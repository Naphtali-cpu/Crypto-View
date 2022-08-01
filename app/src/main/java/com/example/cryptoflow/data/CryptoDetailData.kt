package com.example.cryptoflow.data

data class CryptoDetailData(
    val id: String,
    val name: String,
    val description: Description
)

data class Description(
    val en: String
)
