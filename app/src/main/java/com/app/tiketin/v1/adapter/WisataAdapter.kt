package com.app.tiketin.v1.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.tiketin.v1.R
import com.app.tiketin.v1.databinding.ItemWisataBinding
import com.app.tiketin.v1.model.WisataItem

class WisataAdapter(
    private var listWisata: List<WisataItem>,
    private val onItemClick: (WisataItem) -> Unit
) : RecyclerView.Adapter<WisataAdapter.ListViewHolder>() {

    fun updateData(newList: List<WisataItem>) {
        listWisata = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemWisataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val wisata = listWisata[position]
        holder.bind(wisata)
    }

    override fun getItemCount(): Int = listWisata.size

    inner class ListViewHolder(private val binding: ItemWisataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: WisataItem) {
            with(binding) {
                // Mengambil gambar berdasarkan ID wisata (misal id=1 -> wisata1)
                val context = root.context
                val imageName = "wisata${item.id}"
                val resId = context.resources.getIdentifier(imageName, "drawable", context.packageName)
                imgWisata.setImageResource(if (resId != 0) resId else R.drawable.wisata1)

                tvName.text = item.name
                tvLocation.text = item.location
                tvRating.text = "⭐ ${item.rating}"
                
                root.setOnClickListener { onItemClick(item) }
            }
        }
    }
}