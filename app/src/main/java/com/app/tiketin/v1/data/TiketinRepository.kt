package com.app.tiketin.v1.data

import com.app.tiketin.v1.model.WisataItem
import com.google.firebase.database.FirebaseDatabase

object TiketinRepository {

    private val database = FirebaseDatabase.getInstance()

    fun getWisata(
        onSuccess: (List<WisataItem>) -> Unit,
        onError: (String) -> Unit
    ) {
        database.reference
            .child("wisata")
            .get()
            .addOnSuccessListener { snapshot ->
                val list = mutableListOf<WisataItem>()
                snapshot.children.forEach { item ->
                    item.getValue(WisataItem::class.java)?.let {
                        list.add(it)
                    }
                }
                onSuccess(list)
            }
            .addOnFailureListener {
                onError(it.message ?: "Error saat mengambil data dari Firebase")
            }
    }

    fun getWisataById(
        id: Int,
        onSuccess: (WisataItem?) -> Unit,
        onError: (String) -> Unit
    ) {
        database.reference
            .child("wisata")
            .child(id.toString())
            .get()
            .addOnSuccessListener { snapshot ->
                val item = snapshot.getValue(WisataItem::class.java)
                onSuccess(item)
            }
            .addOnFailureListener {
                onError(it.message ?: "Error saat mengambil detail")
            }
    }

    fun syncWisataFromFirebase(
        dbHelper: DatabaseHelper,
        onComplete: (Boolean) -> Unit
    ) {
        android.util.Log.d("TiketinRepository", "Starting sync from Firebase...")
        getWisata(
            onSuccess = { list ->
                android.util.Log.d("TiketinRepository", "Fetched ${list.size} items from Firebase")
                list.forEach { 
                    val rowId = dbHelper.addWisata(it)
                    android.util.Log.d("TiketinRepository", "Inserted/Updated item ${it.name} (ID: ${it.id}) - Result: $rowId")
                }
                onComplete(true)
            },
            onError = {
                android.util.Log.e("TiketinRepository", "Sync error: $it")
                onComplete(false)
            }
        )
    }

    // Fungsi dinonaktifkan agar tidak lagi mengirim data ke Firebase
    fun syncRagunanToFirebase(onComplete: (Boolean) -> Unit) {
        onComplete(true)
    }
}