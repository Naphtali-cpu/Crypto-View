package com.example.cryptoflow.data

import com.google.gson.annotations.SerializedName

data class GraphData(
    @SerializedName("FirstValueInArray") val firstValueInArray: String,
    @SerializedName("Data") val data: List<Data>,
    @SerializedName("TimeFrom") val timeFrom: String,
    @SerializedName("Type") val type: String,
    @SerializedName("Response") val response: String,
    @SerializedName("ConversionType") val conversionType: ConversionType,
    @SerializedName("TimeTo") val timeTo: String,
    @SerializedName("Aggregated") val aggregated: String
) {

    data class ConversionType(val conversionSymbol: String, val type: String)

    data class Data(
        val open: String,
        val time: String,
        val volumeto: String,
        val volumefrom: String,
        val high: String,
        val low: String,
        val close: String
    )
}