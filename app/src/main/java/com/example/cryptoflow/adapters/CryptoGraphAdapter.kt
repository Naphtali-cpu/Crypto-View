package com.example.cryptoflow.adapters

import android.util.Log
import com.example.cryptoflow.data.CryptoData
import com.example.cryptoflow.data.graphmodel.GraphData
import com.example.cryptoflow.data.graphmodel.GraphDataSubList
import com.robinhood.spark.SparkAdapter

class CryptoGraphAdapter(private val graphicalData: ArrayList<GraphDataSubList>) : SparkAdapter() {
    override fun getCount(): Int {
        return graphicalData.size
    }

    override fun getItem(index: Int): Any {
        return graphicalData[index]
    }

    override fun getY(index: Int): Float {
        Log.d("CLOSED", graphicalData[index][4].toFloat().toString())
        return graphicalData[index][4].toFloat()
    }

}