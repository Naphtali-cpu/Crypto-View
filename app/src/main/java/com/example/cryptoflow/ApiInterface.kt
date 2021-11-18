package com.example.cryptoflow

import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @GET("coins/markets?vs_currency=usd&order=market_cap_desc&per_page=100&page=1&sparkline=false")
    fun getCrypto(): Call<List<CryptoData>>


}