package com.app.tiketin.v1.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.app.tiketin.v1.model.HistoryItem
import com.app.tiketin.v1.model.WisataItem
import com.app.tiketin.v1.model.UserProfileData
import com.app.tiketin.v1.model.PaketWisata
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "tiketin.db"
        private const val DATABASE_VERSION = 3
        private const val TAG = "DatabaseHelper"

        private const val TABLE_WISATA = "wisata"
    }

    private val gson = Gson()

    override fun onCreate(db: SQLiteDatabase) {
        Log.d(TAG, "Creating tables...")
        db.execSQL("CREATE TABLE users (" +
                "username TEXT PRIMARY KEY, " +
                "password TEXT, " +
                "nama_lengkap TEXT, " +
                "email TEXT, " +
                "umur INTEGER, " +
                "nomor_ktp TEXT, " +
                "nomor_telepon TEXT)")
        
        db.execSQL("CREATE TABLE $TABLE_WISATA (" +
                "id INTEGER PRIMARY KEY, " +
                "name TEXT, " +
                "location TEXT, " +
                "rating REAL, " +
                "review_count INTEGER, " +
                "price INTEGER, " +
                "description TEXT, " +
                "facilities TEXT, " +
                "operational_hours TEXT, " +
                "phone_number TEXT, " +
                "website TEXT, " +
                "gallery TEXT, " +
                "tour_packages TEXT)")

        db.execSQL("CREATE TABLE history (" +
                "id TEXT PRIMARY KEY, " +
                "username TEXT, " +
                "wisata_name TEXT, " +
                "package_name TEXT, " +
                "price INTEGER, " +
                "date TEXT, " +
                "wisata_id INTEGER)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS users")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_WISATA")
        db.execSQL("DROP TABLE IF EXISTS history")
        onCreate(db)
    }

    fun addWisata(w: WisataItem): Long {
        val db = this.writableDatabase
        val v = ContentValues().apply {
            put("id", w.id)
            put("name", w.name)
            put("location", w.location)
            put("rating", w.rating)
            put("review_count", w.reviewCount)
            put("price", w.price)
            put("description", w.description)
            put("facilities", gson.toJson(w.facilities))
            put("operational_hours", w.operationalHours)
            put("phone_number", w.phoneNumber)
            put("website", w.website)
            put("gallery", gson.toJson(w.gallery))
            put("tour_packages", gson.toJson(w.tourPackages))
        }
        return db.insertWithOnConflict(TABLE_WISATA, null, v, SQLiteDatabase.CONFLICT_REPLACE)
    }

    fun getAllWisata(): List<WisataItem> {
        val list = mutableListOf<WisataItem>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_WISATA", null)
        
        val typeStr: Type = object : TypeToken<List<String>>() {}.type
        val typeInt: Type = object : TypeToken<List<Int>>() {}.type
        val typePaket: Type = object : TypeToken<List<PaketWisata>>() {}.type

        if (cursor.moveToFirst()) {
            do {
                try {
                    list.add(WisataItem(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        name = cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        location = cursor.getString(cursor.getColumnIndexOrThrow("location")),
                        rating = cursor.getDouble(cursor.getColumnIndexOrThrow("rating")),
                        reviewCount = cursor.getInt(cursor.getColumnIndexOrThrow("review_count")),
                        price = cursor.getInt(cursor.getColumnIndexOrThrow("price")),
                        description = cursor.getString(cursor.getColumnIndexOrThrow("description")),
                        facilities = gson.fromJson(cursor.getString(cursor.getColumnIndexOrThrow("facilities")), typeStr),
                        operationalHours = cursor.getString(cursor.getColumnIndexOrThrow("operational_hours")),
                        phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow("phone_number")),
                        website = cursor.getString(cursor.getColumnIndexOrThrow("website")),
                        gallery = gson.fromJson(cursor.getString(cursor.getColumnIndexOrThrow("gallery")), typeInt),
                        tourPackages = gson.fromJson(cursor.getString(cursor.getColumnIndexOrThrow("tour_packages")), typePaket)
                    ))
                } catch (e: Exception) { Log.e(TAG, "Parse error: ${e.message}") }
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun getWisataById(id: Int): WisataItem? = getAllWisata().find { it.id == id }

    fun addUser(u: String, p: String) = writableDatabase.insert("users", null, ContentValues().apply { put("username", u); put("password", p) })
    
    fun getUserProfile(username: String): UserProfileData? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users WHERE username = ?", arrayOf(username))
        var profile: UserProfileData? = null
        if (cursor.moveToFirst()) {
            profile = UserProfileData(
                namaLengkap = cursor.getString(cursor.getColumnIndexOrThrow("nama_lengkap")) ?: "",
                email = cursor.getString(cursor.getColumnIndexOrThrow("email")) ?: "",
                umur = cursor.getInt(cursor.getColumnIndexOrThrow("umur")),
                nomorKtp = cursor.getString(cursor.getColumnIndexOrThrow("nomor_ktp")) ?: "",
                nomorTelepon = cursor.getString(cursor.getColumnIndexOrThrow("nomor_telepon")) ?: ""
            )
        }
        cursor.close()
        return profile
    }

    fun updateUserProfile(username: String, profile: UserProfileData): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("nama_lengkap", profile.namaLengkap)
            put("email", profile.email)
            put("umur", profile.umur)
            put("nomor_ktp", profile.nomorKtp)
            put("nomor_telepon", profile.nomorTelepon)
        }
        return db.update("users", values, "username = ?", arrayOf(username))
    }

    fun addHistory(user: String, h: HistoryItem) = writableDatabase.insert("history", null, ContentValues().apply {
        put("id", h.id)
        put("username", user)
        put("wisata_name", h.wisataName)
        put("package_name", h.packageName)
        put("price", h.price)
        put("date", h.date)
        put("wisata_id", h.wisataId)
    })

    fun getHistoryByUsername(u: String): List<HistoryItem> {
        val l = mutableListOf<HistoryItem>()
        readableDatabase.rawQuery("SELECT * FROM history WHERE username=? ORDER BY date DESC", arrayOf(u)).use { c ->
            if (c.moveToFirst()) {
                do {
                    l.add(HistoryItem(
                        id = c.getString(c.getColumnIndexOrThrow("id")),
                        wisataName = c.getString(c.getColumnIndexOrThrow("wisata_name")),
                        packageName = c.getString(c.getColumnIndexOrThrow("package_name")),
                        price = c.getInt(c.getColumnIndexOrThrow("price")),
                        date = c.getString(c.getColumnIndexOrThrow("date")),
                        wisataId = c.getInt(c.getColumnIndexOrThrow("wisata_id"))
                    ))
                } while (c.moveToNext())
            }
        }
        return l
    }
    
    fun checkUser(u: String, p: String) = readableDatabase.rawQuery("SELECT * FROM users WHERE username=? AND password=?", arrayOf(u, p)).use { it.count > 0 }
    fun isUsernameExists(u: String) = readableDatabase.rawQuery("SELECT * FROM users WHERE username=?", arrayOf(u)).use { it.count > 0 }
    fun deleteHistoryItem(id: String) = writableDatabase.delete("history", "id = ?", arrayOf(id))
}
