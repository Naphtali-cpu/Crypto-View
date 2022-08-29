package com.example.cryptoflow.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoflow.R
import com.example.cryptoflow.data.CryptoData
import com.example.cryptoflow.mainui.CoinDetails
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cryptolist.view.*
import java.text.NumberFormat
import java.util.*


class CryptoListAdapter(val context: Context, var userList: List<CryptoData>) : RecyclerView.Adapter<CryptoListAdapter.CryptoViewHolder>(), Filterable {
    var photosList: ArrayList<CryptoData> = ArrayList()
    inner class CryptoViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var cryptoName: TextView
        var initials: TextView
        var cryptoPrice: TextView
        var cryptoImage: ImageView
        var pricePercent: TextView
        var listCoin: ConstraintLayout
        init {
            cryptoName = view.cryptoname
            initials = view.initials
            cryptoPrice = view.cryptoprice
            cryptoImage = view.logo
            pricePercent = view.percent
            listCoin = view.coinList
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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CryptoViewHolder, position: Int) {
        holder.cryptoName.text = userList[position].name
        holder.initials.text = userList[position].symbol.uppercase()
        holder.cryptoPrice.text = "$ ${NumberFormat.getInstance().format(userList[position].current_price)}"

        val imageLink = holder.cryptoImage.logo
        Picasso.with(holder.cryptoImage.context).load(userList[position].image).into(imageLink)

//        If number is negative logic changing the color

        if (0.toString() <= userList[position].price_change_percentage_24h.toString()) {
            holder.pricePercent.setTextColor(context.resources.getColor(R.color.green))
            holder.pricePercent.text = "${userList[position].price_change_percentage_24h} %"
        } else {
            holder.pricePercent.setTextColor(context.resources.getColor(R.color.red))
            holder.pricePercent.text = "${userList[position].price_change_percentage_24h} %"
        }
//        Log.i("FIRST", userList[position].price_change_percentage_24h.toString())
//        Pass data to the next activity and redirection

        holder.listCoin.setOnClickListener { v ->
            val context = v.context
            val intent = Intent(context, CoinDetails::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("id", userList[position].id)
            intent.putExtra("percentCoin", userList[position].price_change_percentage_24h.toString())
            intent.putExtra("name", userList[position].name)
            intent.putExtra("marketcap", userList[position].market_cap)
            intent.putExtra("lastupdate", userList[position].last_updated)
            intent.putExtra("totalvolume", userList[position].total_volume)
            intent.putExtra("currentprice", NumberFormat.getInstance().format(userList[position].current_price))
            intent.putExtra("popularity", userList[position].market_cap_rank)
            intent.putExtra("logourl", userList[position].image)
            context.startActivity(intent)
        }
    }


    override fun getFilter(): Filter {
        return myFilter
    }

    var myFilter: Filter = object : Filter() {
        override fun performFiltering(charSequence: CharSequence?): FilterResults {
            val charSearch = charSequence.toString()
            val filteredList: MutableList<CryptoData> = ArrayList()
            if (charSequence == null || charSequence.length == 0) {
                filteredList.addAll(userList)
            } else {
                for (row in userList) {
                    if (row.name.lowercase(Locale.ROOT).contains(charSearch.lowercase(Locale.ROOT))) {
                        filteredList.add(row)
                    }
                }
                userList = filteredList
            }

            val filterResults = FilterResults()
            filterResults.values = userList
            return filterResults

//            val filterResult = FilterResults()
//            filterResult.values = filteredList
//            return filterResult
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun publishResults(charSequence: CharSequence?, filterResult: FilterResults) {
            userList = filterResult.values as ArrayList<CryptoData>

            notifyDataSetChanged()
        }

    }
}