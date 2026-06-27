package com.app.tiketin.v1.data

import com.app.tiketin.v1.R
import com.app.tiketin.v1.model.PaketWisata
import com.app.tiketin.v1.model.WisataItem

object TiketinRepository {

    fun getWisata(): List<WisataItem> {
        return listOf(
            WisataItem(
                id = 1,
                name = "Raja Ampat",
                location = "Kabupaten Raja Ampat, Papua Barat",
                rating = 4.9,
                reviewCount = 1250,
                price = 5000000,
                description = "Raja Ampat adalah surga bawah laut dunia yang terletak di jantung Segitiga Karang Dunia. Kepulauan ini memiliki kekayaan hayati laut yang tak tertandingi, dengan lebih dari 1.500 jenis ikan dan 75% spesies karang dunia ditemukan di sini.\n\nSelain keindahan bawah lautnya, Anda juga dapat menikmati pemandangan gugusan pulau karst yang memukau seperti di Wayag atau Piaynemo. Aktivitas populer di sini meliputi diving, snorkeling, bird watching (melihat burung Cendrawasih), dan kayaking menyusuri hutan bakau.",
                facilities = listOf("Pusat Informasi", "Penyewaan Alat Diving", "Pemandu Wisata", "Resort Eksotis", "Restoran Seafood"),
                operationalHours = "24 Jam (Setiap Hari)",
                phoneNumber = "+62 812 3456 7890",
                website = "www.rajaampat-wisata.com",
                gallery = listOf(R.drawable.wisata1, R.drawable.wisata2, R.drawable.wisata3),
                tourPackages = listOf(
                    PaketWisata("Paket Snorkeling 3 Hari", 5000000, "3 Hari 2 Malam", listOf("Penginapan", "Makan 3x", "Alat Snorkeling")),
                    PaketWisata("Paket Diving Pro", 8500000, "5 Hari 4 Malam", listOf("Lisensi Diving", "Tabung Oksigen", "Penginapan Mewah"))
                )
            ),
            WisataItem(
                id = 2,
                name = "Gunung Bromo",
                location = "Pasuruan, Jawa Timur",
                rating = 4.8,
                reviewCount = 3400,
                price = 350000,
                description = "Gunung Bromo merupakan salah satu gunung berapi aktif yang paling terkenal di Indonesia. Terletak di dalam Taman Nasional Bromo Tengger Semeru, kawasan ini menawarkan hamparan lautan pasir yang luas dan kawah yang terus mengepulkan asap putih.\n\nMomen paling ikonik adalah melihat matahari terbit dari Puncak Penanjakan, di mana Anda bisa melihat siluet Bromo, Batok, dan Semeru yang gagah berselimut kabut. Perjalanan menuju kawah bisa dilakukan dengan berjalan kaki atau menyewa kuda melewati padang pasir.",
                facilities = listOf("Jeep 4x4", "Kuda Wisata", "Toilet Umum", "Warung Makan", "Toko Souvenir", "Parkir Area"),
                operationalHours = "03.00 - 18.00 WIB",
                phoneNumber = "+62 821 9876 5432",
                website = "www.bromotengger.id",
                gallery = listOf(R.drawable.wisata2, R.drawable.wisata3, R.drawable.wisata1),
                tourPackages = listOf(
                    PaketWisata("Open Trip Sunrise", 350000, "12 Jam", listOf("Transport Jeep", "Tiket Masuk", "Sarapan")),
                    PaketWisata("Private Luxury Bromo", 1500000, "2 Hari 1 Malam", listOf("Hotel Berbintang", "Jeep Private", "Dokumentasi"))
                )
            ),
            WisataItem(
                id = 3,
                name = "Danau Toba",
                location = "Samosir, Sumatera Utara",
                rating = 4.7,
                reviewCount = 2100,
                price = 500000,
                description = "Danau Toba adalah danau vulkanik terbesar di dunia dan salah satu keajaiban alam Indonesia. Terbentuk dari letusan supervolcano ribuan tahun lalu, danau ini memiliki Pulau Samosir di tengahnya yang ukurannya hampir sebesar Singapura.\n\nPengunjung dapat menikmati ketenangan air danau, mengunjungi desa wisata Tomok dan Simanindo untuk mengenal budaya Batak, atau sekadar menikmati kopi Sidikalang sambil menatap pemandangan perbukitan hijau yang mengelilingi danau.",
                facilities = listOf("Kapal Penyeberangan", "Penginapan (Homestay)", "Area Berenang", "Sewa Motor", "Spot Foto"),
                operationalHours = "08.00 - 20.00 WIB",
                phoneNumber = "+62 852 1122 3344",
                website = "www.tobadiscovery.com",
                gallery = listOf(R.drawable.wisata3, R.drawable.wisata1, R.drawable.wisata2),
                tourPackages = listOf(
                    PaketWisata("Island Hopping Samosir", 500000, "8 Jam", listOf("Sewa Kapal", "Pemandu Lokal", "Makan Siang")),
                    PaketWisata("Batak Cultural Experience", 1200000, "3 Hari 2 Malam", listOf("Kursus Tari", "Stay di Rumah Adat", "Full Board Meal"))
                )
            )
        )
    }

    fun getWisataById(id: Int): WisataItem? {
        return getWisata().find { it.id == id }
    }
}
