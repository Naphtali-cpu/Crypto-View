package com.example.cryptoflow

data class NewsData(
    val count: Int,
    val next_url: String,
    val request_id: String,
    val results: List<Result>,
    val status: String,
    val title: String,
    val image_url: String,
    val published_utc: String

    )
data class Result(
    val amp_url: String,
    val article_url: String,
    val author: String,
    val description: String,
    val id: String,
    val keywords: List<String>,
    val publisher: Publisher,
    val tickers: List<String>,
)