package com.example.androidbaseapp.presentation.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.androidbaseapp.data.repositories.model.ArticleModel

class ArticleAdapter : PagingDataAdapter<ArticleModel, ArticleModelViewHolder>(diffUtilArticle) {
    companion object {
        private val diffUtilArticle =
            object : DiffUtil.ItemCallback<ArticleModel>() {
                override fun areItemsTheSame(
                    oldItem: ArticleModel,
                    newItem: ArticleModel
                ): Boolean =
                    oldItem.author == newItem.author && oldItem.urlToImage == newItem.urlToImage

                override fun areContentsTheSame(
                    oldItem: ArticleModel,
                    newItem: ArticleModel
                ): Boolean = oldItem == newItem
            }
    }

    override fun onBindViewHolder(holder: ArticleModelViewHolder, position: Int) {
        holder.bindView(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleModelViewHolder {
        return ArticleModelViewHolder.create(parent)
    }
}