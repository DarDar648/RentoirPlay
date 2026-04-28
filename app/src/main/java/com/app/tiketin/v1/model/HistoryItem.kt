package com.app.tiketin.v1.model

data class HistoryItem(
    val id: String,
    val wisataName: String,
    val packageName: String,
    val price: Int,
    val date: String,
    val imageResId: Int
)