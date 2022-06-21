package com.example.cryptoflow.api

import com.example.cryptoflow.mainui.data.CryptoData
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @GET("coins/markets?vs_currency=usd&order=market_cap_desc&per_page=100&page=1&sparkline=false")
    fun getCrypto(): Call<List<CryptoData>>

    @GET("coins/{id}")
    fun getCurrentData(@Path("id") id: String) : Call<Unit>


}