package com.example.androidbaseapp.common

import com.example.androidbaseapp.domain.interactor.types.DailyWorldData
import com.example.androidbaseapp.presentation.customview.barchart.ColumnModel
import com.example.androidbaseapp.presentation.customview.barchart.DetailColumnModelStatusValue
import com.example.androidbaseapp.presentation.customview.barchart.TabItemModel

fun List<DailyWorldData>.toTabLabelData(): List<TabItemModel> {
    return this.map { TabItemModel(it.status.value) }.distinct()
}

fun List<DailyWorldData>.toColumnData(): List<ColumnModel> {
    val listLabel = this.map { it.label }.distinct().sortedByDescending { it }.reversed()
    val result = mutableListOf<ColumnModel>().apply {
        listLabel.forEachIndexed { index, value ->
            add(ColumnModel(id = index, label = value, values = mutableListOf()))
        }
    }
    result.forEach { item ->
        this.forEach { dailyWorldData ->
            if (dailyWorldData.label == item.label) {
                item.values.add(
                    DetailColumnModelStatusValue(
                        tabOption = dailyWorldData.status.value,
                        value = dailyWorldData.value
                    )
                )
            }
        }
    }
    return result
}