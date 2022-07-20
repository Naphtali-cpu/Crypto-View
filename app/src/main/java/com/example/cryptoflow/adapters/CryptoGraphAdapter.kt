package com.example.cryptoflow.adapters

import com.example.cryptoflow.data.CryptoData
import com.example.cryptoflow.data.GraphData
import com.robinhood.spark.SparkAdapter

class CryptoGraphAdapter(private val graphicalData: List<CryptoData>) : SparkAdapter() {
    override fun getCount() = graphicalData.size

    override fun getItem(index: Int) = graphicalData[index]


    override fun getY(index: Int): Float {
        val plottedData = graphicalData[index]
        return plottedData.high_24h
    }


}