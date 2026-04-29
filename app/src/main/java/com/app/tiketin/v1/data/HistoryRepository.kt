package com.app.tiketin.v1.data

import android.content.Context
import android.content.SharedPreferences
import com.app.tiketin.v1.model.HistoryItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object HistoryRepository {

    private const val PREF_NAME = "tiketin_history"
    private const val KEY_HISTORY_PREFIX = "history_"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    private fun getHistoryKey(username: String): String {
        return "$KEY_HISTORY_PREFIX$username"
    }

    fun addHistory(context: Context, username: String, item: HistoryItem) {
        val currentHistory = getHistory(context, username).toMutableList()
        currentHistory.add(0, item) // Tambahkan di paling atas

        val gson = Gson()
        val json = gson.toJson(currentHistory)

        getPreferences(context).edit().putString(getHistoryKey(username), json).apply()
    }

    fun getHistory(context: Context, username: String): List<HistoryItem> {
        val json = getPreferences(context).getString(getHistoryKey(username), null)

        if (json == null) return emptyList()

        val gson = Gson()
        val type = object : TypeToken<List<HistoryItem>>() {}.type
        return gson.fromJson(json, type)
    }

    fun clearHistory(context: Context, username: String) {
        getPreferences(context).edit().remove(getHistoryKey(username)).apply()
    }

    fun deleteHistoryItem(context: Context, username: String, itemId: String) {
        val currentHistory = getHistory(context, username).toMutableList()
        currentHistory.removeAll { it.id == itemId }

        val gson = Gson()
        val json = gson.toJson(currentHistory)

        getPreferences(context).edit().putString(getHistoryKey(username), json).apply()
    }
}