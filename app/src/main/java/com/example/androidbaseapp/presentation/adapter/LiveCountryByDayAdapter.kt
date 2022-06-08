package com.example.androidbaseapp.presentation.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.androidbaseapp.domain.model.DetailCountryModel

class LiveCountryByDayAdapter
    : PagingDataAdapter<DetailCountryModel, RecyclerView.ViewHolder>(diffUtilLiveCountryModel) {

    companion object {
        private val diffUtilLiveCountryModel =
            object : DiffUtil.ItemCallback<DetailCountryModel>() {
                override fun areItemsTheSame(
                    oldItem: DetailCountryModel,
                    newItem: DetailCountryModel
                ): Boolean = oldItem.country == newItem.country && oldItem.date == newItem.date

                override fun areContentsTheSame(
                    oldItem: DetailCountryModel,
                    newItem: DetailCountryModel
                ): Boolean = oldItem == newItem
            }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? DetailCountryModelViewHolder)?.bindView(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DetailCountryModelViewHolder.create(parent)
    }
}