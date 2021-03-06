package com.example.cryptoflow.api

import com.example.cryptoflow.data.API_KEY
import com.example.cryptoflow.data.APP_NAME
import com.example.cryptoflow.data.CryptoData
import com.example.cryptoflow.data.GraphData
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @GET("coins/markets?vs_currency=usd&order=market_cap_desc&per_page=100&page=1&sparkline=false")
    fun getCrypto(): Call<List<CryptoData>>

    @Headers(API_KEY)
    @GET("{period}")
    suspend fun getCryptoGraph(
        @Path("period") period: String,
        @Query("fsym") fromCurrencySymbol: String?,
        @Query("tsym") toCurrencySymbol: String?,
        @Query("limit") limit: Int,
        @Query("aggregate") aggregate: Int,
        @Query("extraParams") apiKey: String = APP_NAME
    ): GraphData

    @GET("coins/{id}")
    fun getCurrentData(@Path("id") id: String) : Call<Unit>


}