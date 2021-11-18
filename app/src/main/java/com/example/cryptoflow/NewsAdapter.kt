package com.example.cryptoflow

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.newslist.view.*

class NewsAdapter(val homeFeed: HomeFeed): RecyclerView.Adapter<CustomViewHolder>() {

    val videoTitles = listOf("First title", "Second", "3rd", "MOOOOORE TITLE")

    // numberOfItems
    override fun getItemCount(): Int {
        return homeFeed.results.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        // how do we even create a view
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.newslist, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val video = homeFeed.results.get(position)
        val image = holder.view.newsimage
        Picasso.with(holder.view.context).load(video.image_url).into(image)
        val results = homeFeed.results.get(position)
        holder.view.newstitle?.text = results.title
        val publish = homeFeed.results.get(position)
        holder.view.date?.text = publish.published_utc
    }

}

class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view) {

}