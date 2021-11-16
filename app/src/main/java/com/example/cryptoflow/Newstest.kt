package com.example.cryptoflow

data class Newstest(
    val count: Int,
    val next_url: String,
    val request_id: String,
    val results: List<Result>,
    val status: String
)