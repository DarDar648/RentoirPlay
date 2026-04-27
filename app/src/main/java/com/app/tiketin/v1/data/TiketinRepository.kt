package com.app.tiketin.v1.data

import com.app.tiketin.v1.R
import com.app.tiketin.v1.model.WisataItem

object TiketinRepository {

    fun getWisata(): List<WisataItem> {
        return listOf(
            WisataItem(
                1,
                "Raja Ampat",
                "Papua Barat",
                2500000,
                4.9,
                "Surga bawah laut dengan pulau-pulau eksotis dan air jernih.",
                R.drawable.wisata1
            ),
            WisataItem(
                2,
                "Gunung Bromo",
                "Jawa Timur",
                350000,
                4.8,
                "Gunung aktif dengan pemandangan sunrise terbaik di Indonesia.",
                R.drawable.wisata2
            ),
            WisataItem(
                3,
                "Danau Toba",
                "Sumatera Utara",
                500000,
                4.7,
                "Danau vulkanik terbesar dengan pemandangan alam yang indah.",
                R.drawable.wisata3
            )
        )
    }

    fun getWisataById(id: Int): WisataItem? {
        return getWisata().find { it.id == id }
    }
}