package com.app.tiketin.v1.data

import com.app.tiketin.v1.model.HistoryItem

object HistoryRepository {
    private val historyList = mutableListOf<HistoryItem>()

    fun addHistory(item: HistoryItem) {
        historyList.add(0, item) // Add to the top of the list
    }

    fun getHistory(): List<HistoryItem> {
        return historyList
    }
}