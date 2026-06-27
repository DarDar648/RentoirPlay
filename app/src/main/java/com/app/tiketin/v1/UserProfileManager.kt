package com.app.tiketin.v1

import android.content.Context
import android.content.SharedPreferences
import com.app.tiketin.v1.data.DatabaseHelper
import com.app.tiketin.v1.model.UserProfileData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class UserProfileManager(context: Context) {

    private val sharedPref: SharedPreferences = context.getSharedPreferences("user_profile", Context.MODE_PRIVATE)
    private val dbHelper = DatabaseHelper(context)
    private val gson = Gson()

    companion object {
        private const val KEY_PROFILE_PREFIX = "profile_"
    }

    private fun getProfileKey(username: String): String {
        return "$KEY_PROFILE_PREFIX$username"
    }

    fun saveProfile(username: String, profile: UserProfileData) {
        // Simpan ke SharedPreferences
        val json = gson.toJson(profile)
        sharedPref.edit().putString(getProfileKey(username), json).apply()
        
        // Simpan ke Database
        dbHelper.updateUserProfile(username, profile)
    }

    fun getProfile(username: String): UserProfileData {
        // Coba ambil dari database dulu
        val dbProfile = dbHelper.getUserProfile(username)
        if (dbProfile != null && (dbProfile.namaLengkap.isNotBlank() || dbProfile.email.isNotBlank())) {
            return dbProfile
        }

        // Jika di database kosong, coba ambil dari SharedPreferences (fallback)
        val json = sharedPref.getString(getProfileKey(username), null)
        if (json == null) return UserProfileData()

        val type = object : TypeToken<UserProfileData>() {}.type
        return gson.fromJson(json, type)
    }

    fun isProfileComplete(username: String): Boolean {
        val profile = getProfile(username)
        return profile.namaLengkap.isNotBlank() &&
                profile.umur > 0 &&
                profile.nomorKtp.isNotBlank() &&
                profile.nomorTelepon.isNotBlank() &&
                profile.email.isNotBlank()
    }
}