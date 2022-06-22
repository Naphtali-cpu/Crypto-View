package com.example.cryptoflow.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoflow.R
import com.example.cryptoflow.data.CryptoData
import com.example.cryptoflow.data.Tickers
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cryptolist.view.*

class CryptoListAdapter(val context: Context, val userList: List<CryptoData>) : RecyclerView.Adapter<CryptoListAdapter.CryptoViewHolder>(){

    inner class CryptoViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var cryptoName: TextView
        var initials: TextView
        var cryptoPrice: TextView
        var cryptoImage: ImageView
        var pricePercent: TextView
        init {
            cryptoName = view.cryptoname
            initials = view.initials
            cryptoPrice = view.cryptoprice
            cryptoImage = view.logo
            pricePercent = view.percent
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoViewHolder {
        return CryptoViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.cryptolist, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: CryptoViewHolder, position: Int) {
        holder.cryptoName.text = userList[position].name
        holder.initials.text = userList[position].symbol
        holder.cryptoPrice.text = userList[position].current_price
        holder.pricePercent.text = userList[position].price_change_percentage_24h
        val imageLink = holder.cryptoImage.logo
        Picasso.with(holder.cryptoImage.context).load(userList[position].image).into(imageLink)
    }
}