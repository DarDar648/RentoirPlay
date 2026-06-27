package com.app.tiketin.v1

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.tiketin.v1.adapter.HistoryAdapter
import com.app.tiketin.v1.adapter.WisataAdapter
import com.app.tiketin.v1.data.DatabaseHelper
import com.app.tiketin.v1.data.HistoryRepository
import com.app.tiketin.v1.data.TiketinRepository
import com.app.tiketin.v1.databinding.ActivityMainBinding
import com.app.tiketin.v1.model.WisataItem

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sessionManager: UserSessionManager
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var wisataAdapter: WisataAdapter
    private lateinit var historyAdapter: HistoryAdapter
    private var allWisata = listOf<WisataItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = UserSessionManager(this)
        dbHelper = DatabaseHelper(this)

        // Isi/Seeding Data Wisata ke SQLite jika masih kosong
        loadAndSeedWisata()

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

    private fun loadAndSeedWisata() {
        allWisata = dbHelper.getAllWisata()
        if (allWisata.isEmpty()) {
            val initialData = TiketinRepository.getWisata()
            initialData.forEach { dbHelper.addWisata(it) }
            allWisata = dbHelper.getAllWisata()
        }
    }

    private fun setupHeader() {
        val username = sessionManager.getUsername()
        binding.incHeader.tvGreeting.text = "Selamat Datang, $username!"
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

        historyAdapter = HistoryAdapter(emptyList()) { item ->
            showDeleteConfirmationDialog(item.id)
        }
    }

    private fun showDeleteConfirmationDialog(itemId: String) {
        AlertDialog.Builder(this)
            .setTitle("Hapus Riwayat")
            .setMessage("Apakah Anda yakin ingin menghapus riwayat pembelian ini?")
            .setPositiveButton("Hapus") { _, _ ->
                HistoryRepository.deleteHistoryItem(this, itemId)
                Toast.makeText(this, "Riwayat dihapus", Toast.LENGTH_SHORT).show()
                showHistoryView() // Refresh history view
            }
            .setNegativeButton("Batal", null)
            .show()
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
        allWisata = dbHelper.getAllWisata()
        wisataAdapter.updateData(allWisata)
    }

    private fun showHistoryView() {
        binding.tvWelcome.text = "Riwayat Pemesanan"
        binding.incHeader.root.visibility = View.GONE
        binding.incSearch.root.visibility = View.GONE

        val username = sessionManager.getUsername()
        if (username == null) {
            Toast.makeText(this, "Session error! Silakan login ulang.", Toast.LENGTH_SHORT).show()
            return
        }

        val historyList = HistoryRepository.getHistory(this, username)
        binding.rvWisata.adapter = historyAdapter
        historyAdapter.updateData(historyList)
        
        if (historyList.isEmpty()) {
            Toast.makeText(this, "Belum ada riwayat pemesanan", Toast.LENGTH_SHORT).show()
        }
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
        if (binding.bottomNavigation.selectedItemId == R.id.nav_history) {
            showHistoryView()
        } else {
            allWisata = dbHelper.getAllWisata()
            wisataAdapter.updateData(allWisata)
        }
    }
}
