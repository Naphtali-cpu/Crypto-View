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

    @GET("coins/markets?vs_currency=usd")
    fun getCrypto(
        @Query("per_page") per_page: Int,
        @Query("page") page: Int,
        @Query("order") order: String
    ): Call<List<CryptoData>>




    @GET("coins/{id}")
    fun getCurrentData(
        @Path("id") id: String
    ) : Call<TestDetailData>

    @GET("coins/{id}/ohlc?vs_currency=usd")
    fun getGraphData(
        @Path("id") id: String,
        @Query("days") days: Int
    ): Call<ArrayList<GraphDataSubList>>


}