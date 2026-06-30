package com.app.tiketin.v1.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.tiketin.v1.R
import com.app.tiketin.v1.databinding.ItemHistoryBinding
import com.app.tiketin.v1.model.HistoryItem
import java.text.NumberFormat
import java.util.Locale

class HistoryAdapter(
    private var items: List<HistoryItem>,
    private val onDeleteClick: (HistoryItem) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHistoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        with(holder.binding) {

            val context = root.context

            val imageName = when (item.wisataId) {
                1 -> "wisata1"
                2 -> "wisata2"
                3 -> "wisata3"
                4 -> "wisata13"
                5 -> "wisata15"
                6 -> "wisata8"
                else -> "wisata1"
            }

            val resId = context.resources.getIdentifier(
                imageName,
                "drawable",
                context.packageName
            )

            ivHistoryImage.setImageResource(
                if (resId != 0) resId else R.drawable.ic_launcher_background
            )

            tvHistoryWisataName.text = item.wisataName
            tvHistoryPackageName.text = item.packageName
            tvHistoryDate.text = item.date
            tvHistoryPrice.text = formatCurrency(item.price)

            btnDeleteHistory.setOnClickListener {
                onDeleteClick(item)
            }
        }
    }

    override fun getItemCount() = items.size

    fun updateData(newItems: List<HistoryItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    private fun formatCurrency(amount: Int): String {
        val format = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("id-ID"))
        return format.format(amount).replace(",00", "")
    }
}
