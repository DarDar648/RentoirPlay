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
                onError(it.message ?: "Error")
            }
    }

    fun getWisataById(
        id: Int,
        onSuccess: (WisataItem?) -> Unit,
        onError: (String) -> Unit
    ) {

        database.reference
            .child("wisata")
            .get()
            .addOnSuccessListener { snapshot ->

                var result: WisataItem? = null

                snapshot.children.forEach { item ->
                    val wisata = item.getValue(WisataItem::class.java)

                    if (wisata?.id == id) {
                        result = wisata
                    }
                }

                onSuccess(result)
            }
            .addOnFailureListener {
                onError(it.message ?: "Error")
            }
    }
}