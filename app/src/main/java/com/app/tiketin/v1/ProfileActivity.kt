package com.app.tiketin.v1

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.app.tiketin.v1.databinding.ActivityProfileBinding
import com.app.tiketin.v1.model.UserProfileData

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var sessionManager: UserSessionManager
    private lateinit var profileManager: UserProfileManager
    private var currentUsername: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi manager
        sessionManager = UserSessionManager(this)
        profileManager = UserProfileManager(this)
        currentUsername = sessionManager.getUsername()

        // Setup toolbar (tombol back)
        setupToolbar()

        // Load data profile yang sudah tersimpan (jika ada)
        loadProfileData()

        // Setup tombol simpan
        setupSaveButton()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        // Ubah warna tombol back menjadi biru
        val upArrow = AppCompatResources.getDrawable(this, R.drawable.ic_arrow_back)
        if (upArrow != null) {
            upArrow.setTint(getColor(R.color.brand_primary))
            supportActionBar?.setHomeAsUpIndicator(upArrow)
        }
    }

    private fun loadProfileData() {
        val username = currentUsername
        if (username == null) {
            Toast.makeText(this, "Session error! Silakan login ulang.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Tampilkan username di profil
        binding.tvUsername.text = "@$username"

        // Load data profile yang sudah tersimpan
        val profile = profileManager.getProfile(username)

        binding.etNamaLengkap.setText(profile.namaLengkap)
        if (profile.umur > 0) {
            binding.etUmur.setText(profile.umur.toString())
        }
        binding.etNomorKtp.setText(profile.nomorKtp)
        binding.etNomorTelepon.setText(profile.nomorTelepon)
        binding.etEmail.setText(profile.email)
    }

    private fun setupSaveButton() {
        binding.btnSaveProfile.setOnClickListener {
            saveProfile()
        }
    }

    private fun saveProfile() {
        val username = currentUsername
        if (username == null) {
            Toast.makeText(this, "Session error! Silakan login ulang.", Toast.LENGTH_SHORT).show()
            return
        }

        // Ambil data dari form
        val namaLengkap = binding.etNamaLengkap.text.toString().trim()
        val umurText = binding.etUmur.text.toString().trim()
        val nomorKtp = binding.etNomorKtp.text.toString().trim()
        val nomorTelepon = binding.etNomorTelepon.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()

        // ===== VALIDASI =====
        if (namaLengkap.isEmpty()) {
            binding.etNamaLengkap.error = "Nama lengkap harus diisi"
            return
        }

        if (umurText.isEmpty()) {
            binding.etUmur.error = "Umur harus diisi"
            return
        }

        val umur = umurText.toIntOrNull()
        if (umur == null || umur <= 0 || umur > 150) {
            binding.etUmur.error = "Umur tidak valid (1-150)"
            return
        }

        if (nomorKtp.isEmpty()) {
            binding.etNomorKtp.error = "Nomor KTP harus diisi"
            return
        }

        if (nomorTelepon.isEmpty()) {
            binding.etNomorTelepon.error = "Nomor telepon harus diisi"
            return
        }

        if (email.isEmpty()) {
            binding.etEmail.error = "Email harus diisi"
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Email tidak valid"
            return
        }

        // ===== SIMPAN PROFILE =====
        val profile = UserProfileData(
            namaLengkap = namaLengkap,
            umur = umur,
            nomorKtp = nomorKtp,
            nomorTelepon = nomorTelepon,
            email = email
        )

        profileManager.saveProfile(username, profile)

        Toast.makeText(this, "Profil berhasil disimpan!", Toast.LENGTH_SHORT).show()

        // Kembali ke halaman utama setelah save
        finish()
    }
}