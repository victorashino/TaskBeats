package com.comunidadedevspace.taskbeats.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.comunidadedevspace.taskbeats.data.local.News
import com.comunidadedevspace.taskbeats.databinding.ItemNewsBinding

class NewsListViewHolder(
    private val bind: ItemNewsBinding,
    private val view: View
) : RecyclerView.ViewHolder(bind.root) {

    fun bind(news: News) {
        bind.textNewsTitle.text = news.title
        bind.imgNews.load(news.imgUrl) {
            transformations(RoundedCornersTransformation(16f))
        }
    }

}