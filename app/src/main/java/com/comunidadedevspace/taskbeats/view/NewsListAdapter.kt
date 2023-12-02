package com.comunidadedevspace.taskbeats.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.comunidadedevspace.taskbeats.data.local.News
import com.comunidadedevspace.taskbeats.databinding.ItemNewsBinding

class NewsListAdapter : ListAdapter<News, NewsListViewHolder>(NewsListAdapter) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsListViewHolder {
        val view = ItemNewsBinding
            .inflate(
                LayoutInflater
                    .from(parent.context), parent, false
            )
        return NewsListViewHolder(view, view.root)
    }

    override fun onBindViewHolder(holder: NewsListViewHolder, position: Int) {
        val news = getItem(position)
        holder.bind(news)
    }

    companion object : DiffUtil.ItemCallback<News>() {
        override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem.title == newItem.title &&
                    oldItem.imgUrl == newItem.imgUrl
        }

    }
}