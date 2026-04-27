package com.app.tiketin.v1.model

data class WisataItem(
    val id: Int,
    val name: String,
    val location: String,   // ganti dari restaurant
    val price: Int,
    val rating: Double,
    val description: String,
    val imageResId: Int
)