package com.app.tiketin.v1

import android.content.Context
import android.content.SharedPreferences

class UserSessionManager(context: Context) {

    private val sharedPref: SharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
    private val editor = sharedPref.edit()

    companion object {
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USERNAME = "username"
        private const val KEY_REMEMBER_ME = "remember_me"
    }

    // Simpan akun baru saat registrasi
    fun saveUserAccount(username: String, password: String) {
        editor.putString("acc_$username", password)
        editor.apply()
    }

    // Validasi login
    fun validateLogin(username: String, password: String): Boolean {
        val savedPassword = sharedPref.getString("acc_$username", null)
        return savedPassword != null && savedPassword == password
    }

    // Cek apakah username sudah terdaftar
    fun isUsernameExists(username: String): Boolean {
        return sharedPref.contains("acc_$username")
    }

    // Buat session login
    fun createLoginSession(username: String, rememberMe: Boolean) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.putString(KEY_USERNAME, username)
        editor.putBoolean(KEY_REMEMBER_ME, rememberMe)
        editor.apply()
    }

    // Cek apakah sudah login
    fun isLoggedIn(): Boolean {
        return sharedPref.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    // Ambil username yang login
    fun getUsername(): String? {
        return sharedPref.getString(KEY_USERNAME, null)
    }

    // Logout
    fun logout() {
        editor.clear()
        editor.apply()
    }
}