package com.app.tiketin.v1
import android.view.View
import android.widget.TextView
import android.widget.ImageView
import com.app.tiketin.v1.data.TiketinRepository

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.app.tiketin.v1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sessionManager: UserSessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = TiketinRepository.getWisata()

        val item1 = findViewById<View>(R.id.item1)
        val item2 = findViewById<View>(R.id.item2)
        val item3 = findViewById<View>(R.id.item3)

// ITEM 1
        item1.findViewById<TextView>(R.id.tvName).text = data[0].name
        item1.findViewById<TextView>(R.id.tvLocation).text = data[0].location
        item1.findViewById<TextView>(R.id.tvRating).text = "⭐ ${data[0].rating}"
        item1.findViewById<ImageView>(R.id.imgWisata)
            .setImageResource(data[0].imageResId)

// ITEM 2
        item2.findViewById<TextView>(R.id.tvName).text = data[1].name
        item2.findViewById<TextView>(R.id.tvLocation).text = data[1].location
        item2.findViewById<TextView>(R.id.tvRating).text = "⭐ ${data[1].rating}"
        item2.findViewById<ImageView>(R.id.imgWisata)
            .setImageResource(data[1].imageResId)

// ITEM 3
        item3.findViewById<TextView>(R.id.tvName).text = data[2].name
        item3.findViewById<TextView>(R.id.tvLocation).text = data[2].location
        item3.findViewById<TextView>(R.id.tvRating).text = "⭐ ${data[2].rating}"
        item3.findViewById<ImageView>(R.id.imgWisata)
            .setImageResource(data[2].imageResId)

        sessionManager = UserSessionManager(this)

        val username = sessionManager.getUsername()
        binding.tvWelcome.text = "Selamat Datang di Tiketin, $username!"

        binding.btnLogout.setOnClickListener {
            sessionManager.logout()
            Toast.makeText(this, "Logout berhasil!", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // CLICK ITEM 1
        item1.setOnClickListener {
            val intent = Intent(this, WisataDetailActivity::class.java)
            intent.putExtra(WisataDetailActivity.EXTRA_ID, data[0].id)
            startActivity(intent)
        }

// CLICK ITEM 2
        item2.setOnClickListener {
            val intent = Intent(this, WisataDetailActivity::class.java)
            intent.putExtra(WisataDetailActivity.EXTRA_ID, data[1].id)
            startActivity(intent)
        }

// CLICK ITEM 3
        item3.setOnClickListener {
            val intent = Intent(this, WisataDetailActivity::class.java)
            intent.putExtra(WisataDetailActivity.EXTRA_ID, data[2].id)
            startActivity(intent)
        }
    }
}