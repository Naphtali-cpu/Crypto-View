package com.example.cryptoflow

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cryptolist.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CryptoListAdapter(val context: Context, val userList: List<CryptoData>) : RecyclerView.Adapter<CryptoListAdapter.CryptoViewHolder>(){

    class CryptoViewHolder(view : View) : RecyclerView.ViewHolder(view){
        fun bindMovie(movie : CryptoData){
            itemView.cryptoname.text = movie.name
            itemView.initials.text = movie.symbol
            itemView.cryptoprice.text = movie.current_price
            itemView.percent.text = movie.price_change_percentage_24h + "%"
            Picasso.with(itemView.context).load(movie.image).into(itemView.logo)

            itemView.percent.setTextColor(if (movie.price_change_percentage_24h!!.contains("-"))
                Color.parseColor("#FF0000")
            else
                Color.parseColor("#32CD32")
            )
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
        holder.bindMovie(userList.get(position))
    }
}