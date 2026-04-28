package com.app.tiketin.v1

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.tiketin.v1.adapter.WisataAdapter
import com.app.tiketin.v1.data.TiketinRepository
import com.app.tiketin.v1.databinding.ActivityMainBinding
import com.app.tiketin.v1.model.WisataItem

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sessionManager: UserSessionManager
    private lateinit var wisataAdapter: WisataAdapter
    private var allWisata = listOf<WisataItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = UserSessionManager(this)
        allWisata = TiketinRepository.getWisata()

        setupHeader()
        setupRecyclerView()
        setupSearch()

        binding.btnLogout.setOnClickListener {
            sessionManager.logout()
            Toast.makeText(this, "Logout berhasil!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun setupHeader() {
        val username = sessionManager.getUsername()
        // Menggunakan tvGreeting yang ada di view_home_header.xml
        binding.incHeader.tvGreeting.text = "Selamat Datang, $username!"
        // Mengubah judul section destinasi
        binding.tvWelcome.text = "Destinasi Populer"
    }

    private fun setupRecyclerView() {
        wisataAdapter = WisataAdapter(allWisata) { wisata ->
            val intent = Intent(this, WisataDetailActivity::class.java)
            intent.putExtra(WisataDetailActivity.EXTRA_ID, wisata.id)
            startActivity(intent)
        }
        
        binding.rvWisata.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = wisataAdapter
            isNestedScrollingEnabled = false
        }
    }

    private fun setupSearch() {
        // etSearch berada di dalam incSearch (view_home_search.xml)
        binding.incSearch.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterData(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun filterData(query: String) {
        val filteredList = if (query.isEmpty()) {
            allWisata
        } else {
            allWisata.filter {
                it.name.contains(query, ignoreCase = true) || 
                it.location.contains(query, ignoreCase = true)
            }
        }
        wisataAdapter.updateData(filteredList)
    }
}