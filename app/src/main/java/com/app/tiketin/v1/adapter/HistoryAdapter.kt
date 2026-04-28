package com.app.tiketin.v1.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.tiketin.v1.databinding.ItemHistoryBinding
import com.app.tiketin.v1.model.HistoryItem
import java.text.NumberFormat
import java.util.Locale

class HistoryAdapter(private var items: List<HistoryItem>) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

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
            ivHistoryImage.setImageResource(item.imageResId)
            tvHistoryWisataName.text = item.wisataName
            tvHistoryPackageName.text = item.packageName
            tvHistoryDate.text = item.date
            tvHistoryPrice.text = formatCurrency(item.price)
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