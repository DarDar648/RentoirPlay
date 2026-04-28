package com.app.tiketin.v1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.tiketin.v1.data.TiketinRepository
import com.app.tiketin.v1.databinding.ActivityWisataDetailBinding

class WisataDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ID = "extra_id"
    }

    private lateinit var binding: ActivityWisataDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWisataDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getIntExtra(EXTRA_ID, -1)
        val item = TiketinRepository.getWisataById(id)

        if (item != null) {
            binding.imgWisata.setImageResource(item.imageResId)
            binding.tvName.text = item.name
            binding.tvLocation.text = item.location
            binding.tvRating.text = "⭐ ${item.rating}"
            binding.tvDesc.text = item.description
        }
    }
}