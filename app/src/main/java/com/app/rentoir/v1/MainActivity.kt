package com.app.rentoir.v1 // Sesuaikan dengan namespace di build.gradle kamu

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.rentoir.v1.databinding.ActivityMainBinding
import android.content.Intent

class MainActivity : AppCompatActivity() {

    // Inisialisasi binding
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Menghubungkan Kotlin dengan layout activity_main.xml
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /* KARENA REFACTORING (PERTEMUAN 3):
           ID seperti tvGreeting sekarang berada di dalam layout 'incHeader'
           ID seperti etSearch sekarang berada di dalam layout 'incSearch'
        */

        // 1. Mengatur Header (dari view_home_header.xml)
        binding.incHeader.tvGreeting.text = "Selamat datang 👋"
        binding.incHeader.tvLocation.text = "UPNVJ, Jakarta"

        // 2. Mengatur Search Bar (dari view_home_search.xml)
        binding.incSearch.etSearch.hint = "Cari makanan, minuman, atau restoran"

        // 3. Mengatur Promo (dari view_home_promo.xml)
        // Contoh: Jika ingin membuat tombol "Lihat Menu" bisa diklik
        binding.incPromo.btnExplore.setOnClickListener {
            startActivity(Intent(this, MenuListActivity::class.java))
        }

    }
}