package com.example.androidbaseapp.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidbaseapp.R
import com.example.androidbaseapp.domain.model.DetailCountryModel

class DetailCountryModelViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val tvCountryName: TextView by lazy { view.findViewById(R.id.tvCountryName) }
    companion object {
        fun create(parent: ViewGroup): DetailCountryModelViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.home_country_item_layout, parent, false)
            return DetailCountryModelViewHolder(view)
        }
    }

    fun bindView(item: DetailCountryModel?) {
        tvCountryName.text = item?.country
    }
}