package com.app.tiketin.v1.model

data class WisataItem(
    val id: Int = 0,
    val name: String = "",
    val location: String = "",
    val rating: Double = 0.0,
    val reviewCount: Int = 0,
    val price: Int = 0,
    val description: String = "",
    val facilities: List<String> = emptyList(),
    val operationalHours: String = "",
    val phoneNumber: String = "",
    val website: String = "",
    val gallery: List<String> = emptyList(),
    val tourPackages: List<PaketWisata> = emptyList(),
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)
