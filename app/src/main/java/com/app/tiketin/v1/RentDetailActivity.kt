package com.app.tiketin.v1

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
    private val rupiah = NumberFormat.getCurrencyInstance(Locale("id", "ID"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRentDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            Toast.makeText(this, "Dipilih: ${item.name}", Toast.LENGTH_SHORT).show()
        }
    }
}