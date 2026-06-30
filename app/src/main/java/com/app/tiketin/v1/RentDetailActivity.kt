package com.app.tiketin.v1

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.tiketin.v1.data.TiketinRepository
import com.app.tiketin.v1.databinding.ActivityRentDetailBinding
import java.text.NumberFormat
import java.util.Locale

class RentDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_RENT_ID = "extra_rent_id"
    }

    private lateinit var binding: ActivityRentDetailBinding
    private lateinit var sessionManager: UserSessionManager
    private lateinit var profileManager: UserProfileManager
    private val rupiah = NumberFormat.getCurrencyInstance(Locale("id", "ID"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRentDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = UserSessionManager(this)
        profileManager = UserProfileManager(this)

        val id = intent.getIntExtra(EXTRA_RENT_ID, -1)

        val item = TiketinRepository.getWisataById(id)

        if (item == null) {
            Toast.makeText(this, "Data wisata tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Mengambil gambar berdasarkan ID wisata
        val imageName = "wisata${item.id}"
        val resId = resources.getIdentifier(imageName, "drawable", packageName)
        binding.imgRent.setImageResource(if (resId != 0) resId else R.drawable.wisata1)

        binding.tvName.text = item.name
        binding.tvMeta.text = "${item.location} • ⭐ ${item.rating}"
        binding.tvPrice.text = rupiah.format(item.price)
        binding.tvDesc.text = item.description

        binding.btnAddToCart.setOnClickListener {
            val username = sessionManager.getUsername()
            if (username != null) {
                if (profileManager.isProfileComplete(username)) {
                    Toast.makeText(this, "Berhasil memesan: ${item.name}", Toast.LENGTH_SHORT).show()
                } else {
                    showCompleteProfileDialog()
                }
            } else {
                Toast.makeText(this, "Silakan login terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showCompleteProfileDialog() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Data Diri Belum Lengkap")
            .setMessage("Silakan lengkapi data diri Anda terlebih dahulu sebelum melakukan pemesanan.")
            .setPositiveButton("Lengkapi Sekarang") { _, _ ->
                startActivity(Intent(this, ProfileActivity::class.java))
            }
            .setNegativeButton("Nanti", null)
            .show()
    }
}