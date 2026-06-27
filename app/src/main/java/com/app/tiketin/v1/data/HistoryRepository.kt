package com.app.tiketin.v1.data

import android.content.Context
import com.app.tiketin.v1.model.HistoryItem

object HistoryRepository {

    fun addHistory(context: Context, username: String, item: HistoryItem) {
        val dbHelper = DatabaseHelper(context)
        dbHelper.addHistory(username, item)
    }

    fun getHistory(context: Context, username: String): List<HistoryItem> {
        val dbHelper = DatabaseHelper(context)
        return dbHelper.getHistoryByUsername(username)
    }

    fun deleteHistoryItem(context: Context, itemId: String) {
        val dbHelper = DatabaseHelper(context)
        dbHelper.deleteHistoryItem(itemId)
    }

    // Fungsi lama untuk kompatibilitas jika masih ada yang memanggil tanpa username
    // Namun sebaiknya semua diarahkan ke fungsi yang butuh username
}
