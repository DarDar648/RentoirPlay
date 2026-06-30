package com.app.tiketin.v1.data

import com.app.tiketin.v1.model.PaketWisata
import com.app.tiketin.v1.model.WisataItem
import com.google.firebase.database.FirebaseDatabase

object TiketinRepository {

    private val database = FirebaseDatabase.getInstance()

    fun getWisata(
        onSuccess: (List<WisataItem>) -> Unit,
        onError: (String) -> Unit
    ) {
        database.reference
            .child("wisata")
            .get()
            .addOnSuccessListener { snapshot ->
                val list = mutableListOf<WisataItem>()
                snapshot.children.forEach { item ->
                    item.getValue(WisataItem::class.java)?.let {
                        list.add(it)
                    }
                }
                onSuccess(list)
            }
            .addOnFailureListener {
                onError(it.message ?: "Error saat mengambil data dari Firebase")
            }
    }

    fun getWisataById(
        id: Int,
        onSuccess: (WisataItem?) -> Unit,
        onError: (String) -> Unit
    ) {
        database.reference
            .child("wisata")
            .child(id.toString())
            .get()
            .addOnSuccessListener { snapshot ->
                val item = snapshot.getValue(WisataItem::class.java)
                onSuccess(item)
            }
            .addOnFailureListener {
                onError(it.message ?: "Error saat mengambil detail")
            }
    }

    fun syncRagunanToFirebase(onComplete: (Boolean) -> Unit) {
        val nusaPenida = WisataItem(
            id = 4,
            name = "Nusa Penida",
            location = "Klungkung, Bali",
            rating = 4.8,
            reviewCount = 2800,
            price = 750000,
            description = "Nusa Penida adalah pulau terbesar dari tiga pulau di tenggara Bali. Pulau ini menawarkan pemandangan tebing yang dramatis dan pantai yang masih perawan seperti Kelingking Beach, Broken Beach, dan Angel's Billabong.",
            facilities = listOf("Sewa Motor", "Pemandu Lokal", "Restoran", "Spot Foto"),
            operationalHours = "24 Jam",
            phoneNumber = "+62 812 0000 1111",
            website = "www.nusapenida.id",
            gallery = listOf("wisata1", "wisata2", "wisata3"),
            tourPackages = listOf(
                PaketWisata("One Day Trip West", 750000, "10 Jam", listOf("Transport", "Tiket Masuk", "Makan Siang"))
            ),
            latitude = -8.7280,
            longitude = 115.5444
        )

        val ragunan = WisataItem(
            id = 5,
            name = "Kebun Binatang Ragunan",
            location = "Pasar Minggu, Jakarta Selatan",
            rating = 4.5,
            reviewCount = 5200,
            price = 4000,
            description = "Taman Margasatwa Ragunan adalah kebun binatang pertama di Indonesia. Memiliki luas 140 hektar dengan koleksi satwa lengkap.",
            facilities = listOf("Pusat Primata Schmutzer", "Area Piknik", "Sewa Sepeda", "Kereta Keliling"),
            operationalHours = "07.00 - 16.00 WIB",
            phoneNumber = "+62 21 7884 7114",
            website = "ragunanzoo.jakarta.go.id",
            gallery = listOf("wisata4", "wisata5", "wisata6", "wisata7"),
            tourPackages = listOf(
                PaketWisata("Tiket Masuk Dewasa", 4000, "Satu Hari", listOf("Akses Area", "Asuransi"))
            ),
            latitude = -6.3124,
            longitude = 106.8202
        )

        val ancol = WisataItem(
            id = 6,
            name = "Taman Impian Jaya Ancol",
            location = "Pademangan, Jakarta Utara",
            rating = 4.6,
            reviewCount = 8500,
            price = 30000,
            description = "Taman Impian Jaya Ancol merupakan objek wisata tematik terbesar dan terkengkap di Jakarta.",
            facilities = listOf("Kereta Gantung (Gondola)", "Bus Wara-Wiri", "Area Kuliner", "Pantai"),
            operationalHours = "06.00 - 22.00 WIB",
            phoneNumber = "+62 21 2922 2222",
            website = "www.ancol.com",
            gallery = listOf("wisata8", "wisata9", "wisata10"),
            tourPackages = listOf(
                PaketWisata("Tiket Masuk Gerbang Ancol", 30000, "Satu Kali Masuk", listOf("Akses Area Pantai dan Taman"))
            ),
            latitude = -6.1256,
            longitude = 106.8436
        )

        val updates = mapOf(
            "4" to nusaPenida,
            "5" to ragunan,
            "6" to ancol
        )

        database.reference.child("wisata").updateChildren(updates)
            .addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
            }
    }
}
