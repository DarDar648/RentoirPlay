package com.app.tiketin.v1.model

data class WisataItem(
    val id: Int,
    val name: String,
    val location: String,
    val rating: Double,
    val reviewCount: Int,
    val price: Int,
    val description: String,
    val facilities: List<String>,
    val operationalHours: String,
    val phoneNumber: String,
    val website: String,
    val imageResId: Int,
    val gallery: List<Int>,
    val tourPackages: List<PaketWisata>
)