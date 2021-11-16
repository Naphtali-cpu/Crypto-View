package com.example.cryptoflow

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.newslist.view.*

class NewsAdapter(val context: Context, val userList: List<NewsData>) : RecyclerView.Adapter<NewsAdapter.CryptoHolderNews>(){

    class CryptoHolderNews(view : View) : RecyclerView.ViewHolder(view){
        fun bindMovie(movie: NewsData){
            itemView.newstitle.text = movie.title
            itemView.date.text = movie.published_utc
            Picasso.with(itemView.context).load(movie.image_url).into(itemView.newsimage)

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoHolderNews {
        return CryptoHolderNews(
            LayoutInflater.from(parent.context).inflate(R.layout.newslist, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: CryptoHolderNews, position: Int) {
        holder.bindMovie(userList.get(position))
    }
}