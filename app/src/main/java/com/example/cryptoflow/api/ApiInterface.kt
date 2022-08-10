package com.example.cryptoflow.api

import com.example.cryptoflow.data.API_KEY
import com.example.cryptoflow.data.APP_NAME
import com.example.cryptoflow.data.CryptoData
import com.example.cryptoflow.data.cryptodetailmodel.TestDetailData
import com.example.cryptoflow.data.graphmodel.GraphData
import com.example.cryptoflow.data.graphmodel.GraphDataSubList
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

//    Get the first two coins

    @GET("coins/markets?vs_currency=usd&order=market_cap_desc")
    fun getCrypto(
        @Query("per_page") per_page: Int,
        @Query("page") page: Int
    ): Call<List<CryptoData>>

//    @Headers(API_KEY)
//    @GET("{period}")
//    suspend fun getCryptoGraph(
//        @Path("period") period: String,
//        @Query("fsym") fromCurrencySymbol: String?,
//        @Query("tsym") toCurrencySymbol: String?,
//        @Query("limit") limit: Int,
//        @Query("aggregate") aggregate: Int,
//        @Query("extraParams") apiKey: String = APP_NAME
//    ): GraphData

    @GET("coins/{id}")
    fun getCurrentData(@Path("id") id: String) : Call<TestDetailData>

    @GET("coins/{id}/ohlc?vs_currency=usd&days=1")
    fun getGraphData(
        @Path("id") id: String
    ): Call<ArrayList<GraphDataSubList>>


}