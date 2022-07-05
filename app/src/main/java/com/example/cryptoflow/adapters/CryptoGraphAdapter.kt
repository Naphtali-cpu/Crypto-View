package com.example.cryptoflow.adapters

import com.example.cryptoflow.data.GraphData
import com.robinhood.spark.SparkAdapter

class CryptoGraphAdapter(private val graphicalData: List<GraphData.Data>, private val baseLineValue: String?) : SparkAdapter() {
    override fun getCount(): Int {
        return graphicalData.size
    }

    override fun getItem(index: Int): GraphData.Data {
        return graphicalData[index]
    }

    override fun getY(index: Int): Float {
        return graphicalData[index].close.toFloat()
    }

    override fun hasBaseLine(): Boolean {
        return true
    }

    override fun getBaseLine(): Float {
        return baseLineValue?.toFloat() ?: super.getBaseLine()
    }

}