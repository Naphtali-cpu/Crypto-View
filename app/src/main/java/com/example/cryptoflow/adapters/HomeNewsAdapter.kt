package com.example.cryptoflow.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoflow.R
import com.example.cryptoflow.mainui.HomeFeed
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.homenews.view.*
import kotlinx.android.synthetic.main.newslist.view.*

class HomeNewsAdapter(val homeFeed: HomeFeed): RecyclerView.Adapter<CustomViewHolder>() {

    // numberOfItems
    override fun getItemCount(): Int {
        return homeFeed.results.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        // how do we even create a view
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.homenews, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val video = homeFeed.results.get(position)
        val image = holder.view.homeNewsImage
        Picasso.with(holder.view.context).load(video.image_url).into(image)
        val results = homeFeed.results.get(position)
        holder.view.homeNewsTitle?.text = results.title
    }

}

class HomeViewHolder(val view: View): RecyclerView.ViewHolder(view) {

}