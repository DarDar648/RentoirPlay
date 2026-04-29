package com.app.tiketin.v1

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.tiketin.v1.adapter.HistoryAdapter
import com.app.tiketin.v1.adapter.WisataAdapter
import com.app.tiketin.v1.data.HistoryRepository
import com.app.tiketin.v1.data.TiketinRepository
import com.app.tiketin.v1.databinding.ActivityMainBinding
import com.app.tiketin.v1.model.WisataItem

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sessionManager: UserSessionManager
    private lateinit var wisataAdapter: WisataAdapter
    private lateinit var historyAdapter: HistoryAdapter
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
        setupBottomNavigation()

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
        binding.incHeader.tvGreeting.text = "Selamat Datang, $username!"
    }

    private fun setupRecyclerView() {
        // Setup Wisata Adapter
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

        // Setup History Adapter
        historyAdapter = HistoryAdapter(emptyList())
    }

    private fun setupSearch() {
        binding.incSearch.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterData(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_search -> {
                    showHomeView()
                    binding.incSearch.etSearch.requestFocus()
                    true
                }
                R.id.nav_history -> {
                    showHistoryView()
                    true
                }
                R.id.nav_profile -> {
                    navigateToProfile()
                    true
                }
                else -> false
            }
        }
    }

    private fun navigateToProfile() {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }
    private fun showHomeView() {
        binding.tvWelcome.text = "Destinasi Populer"
        binding.incHeader.root.visibility = View.VISIBLE
        binding.incSearch.root.visibility = View.VISIBLE
        binding.rvWisata.adapter = wisataAdapter
        wisataAdapter.updateData(allWisata)
    }

    private fun showHistoryView() {
        binding.tvWelcome.text = "Riwayat Pemesanan"
        binding.incHeader.root.visibility = View.GONE
        binding.incSearch.root.visibility = View.GONE

        // Dapatkan username yang sedang login
        val username = sessionManager.getUsername()

        if (username == null) {
            Toast.makeText(this, "Session error! Silakan login ulang.", Toast.LENGTH_SHORT).show()
            return
        }

        // Ambil history berdasarkan user yang login
        val historyList = HistoryRepository.getHistory(this, username)

        if (historyList.isEmpty()) {
            Toast.makeText(this, "Belum ada riwayat pemesanan", Toast.LENGTH_SHORT).show()
        }

        binding.rvWisata.adapter = historyAdapter
        historyAdapter.updateData(historyList)
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

    override fun onResume() {
        super.onResume()
        // Refresh history jika sedang di tab history
        if (binding.bottomNavigation.selectedItemId == R.id.nav_history) {
            showHistoryView()
        }
    }
}