package com.example.cryptoflow.adapters

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoflow.R
import com.example.cryptoflow.api.ApiInterface
import com.example.cryptoflow.api.ServiceBuilder
import com.example.cryptoflow.mainui.data.CryptoData
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
        holder.itemView.cryptoname.setOnClickListener {
            val nextdetail = ServiceBuilder.buildService(ApiInterface::class.java)
            val requestCall = nextdetail.getCurrentData(userList[position].id)

            requestCall.enqueue(object: Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (response.isSuccessful) {
//                        val intent = Intent(context, CryptoDetail::class.java)
//                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                        context.startActivity(intent)

                    } else {
//                        Toast.makeText(this@MyAdapter, "Failed to Delete", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    Log.d("CryptoListAdapter", "onFailure:" + t.message)

                }

            })
        }
    }
}