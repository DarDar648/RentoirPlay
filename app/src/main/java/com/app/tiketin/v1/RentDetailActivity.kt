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
    private val rupiah =
        NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRentDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val id = intent.getIntExtra(EXTRA_RENT_ID, -1)
        val item = TiketinRepository.getRentById(id)
        if (item == null) {
            Toast.makeText(this, "Data menu tidak ditemukan",
                Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        binding.imgRent.setImageResource(item.imageResId)
        binding.tvName.text = item.name
        binding.tvMeta.text = "${item.restaurant} • ⭐ ${item.rating}"
        binding.tvPrice.text = rupiah.format(item.price)
        binding.tvDesc.text = item.description
        binding.btnAddToCart.setOnClickListener {
            Toast.makeText(this, "Ditambahkan: ${item.name}", Toast.LENGTH_SHORT).show()
        }
    }
}
