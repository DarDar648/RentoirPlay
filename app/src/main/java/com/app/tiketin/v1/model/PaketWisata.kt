package com.app.tiketin.v1.model

data class PaketWisata(
    val name: String = "",
    val price: Int = 0,
    val duration: String = "",
    val facilities: List<String> = emptyList()
)