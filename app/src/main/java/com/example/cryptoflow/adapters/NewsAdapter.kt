package com.example.cryptoflow.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoflow.R
import com.example.cryptoflow.mainui.HomeFeed
import com.example.cryptoflow.webview.NewsView
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
        val layoutInflater = LayoutInflater.from(parent.context)
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
        val newsLink = homeFeed.results.get(position)
        holder.view.myNewsList?.setOnClickListener { v ->
            val context = v.context
            val intent = Intent(context, NewsView::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra("URL_LINK", newsLink.article_url)
            context.startActivity(intent)
        }


    }

}

class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view)