package com.example.androidbaseapp.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidbaseapp.R
import com.example.androidbaseapp.common.TimeUtils
import com.example.androidbaseapp.data.repositories.model.ArticleModel

class ArticleModelViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    private val imgNewspaper: ImageView by lazy { view.findViewById(R.id.imgNewspaper) }
    private val tvTitle: TextView by lazy { view.findViewById(R.id.tvTitle) }
    private val tvDatePublic: TextView by lazy { view.findViewById(R.id.tvDatePublic) }
    private val tvAuthor: TextView by lazy { view.findViewById(R.id.tvAuthor) }
    private val tvPreContent: TextView by lazy { view.findViewById(R.id.tvPreContent) }

    companion object {
        fun create(parent: ViewGroup): ArticleModelViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_article_layout, parent, false)
            return ArticleModelViewHolder(view)
        }
    }

    @SuppressLint("CheckResult")
    fun bindView(item: ArticleModel?) {
        imgNewspaper.apply { Glide.with(view).load(item?.urlToImage).centerCrop().into(this) }
        tvTitle.text = item?.title
        tvDatePublic.text = item?.publishedAt
        tvAuthor.text = item?.author
        tvPreContent.text = item?.content
    }
}