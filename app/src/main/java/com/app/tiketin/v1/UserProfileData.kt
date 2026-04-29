package com.app.tiketin.v1.model

data class UserProfileData(
    val namaLengkap: String = "",
    val umur: Int = 0,
    val nomorKtp: String = "",
    val nomorTelepon: String = "",
    val email: String = ""
)