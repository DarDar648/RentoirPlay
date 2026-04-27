package com.app.tiketin.v1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.app.tiketin.v1.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var sessionManager: UserSessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = UserSessionManager(this)

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnRegister.setOnClickListener {
            val username = binding.etNewUsername.text.toString().trim()
            val password = binding.etNewPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Semua field harus diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Password dan konfirmasi password tidak cocok!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 4) {
                Toast.makeText(this, "Password minimal 4 karakter!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (sessionManager.isUsernameExists(username)) {
                Toast.makeText(this, "Username sudah terdaftar! Gunakan username lain.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Simpan akun baru
            sessionManager.saveUserAccount(username, password)
            Toast.makeText(this, "Registrasi berhasil! Silakan login.", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

        binding.tvBackToLogin.setOnClickListener {
            finish()
        }
    }
}