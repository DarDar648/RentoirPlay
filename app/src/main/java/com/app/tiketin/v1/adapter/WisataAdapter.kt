package com.app.tiketin.v1.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.tiketin.v1.R
import com.app.tiketin.v1.databinding.ItemWisataBinding
import com.app.tiketin.v1.databinding.ItemWisataGridBinding
import com.app.tiketin.v1.model.WisataItem

class WisataAdapter(
    private var listWisata: List<WisataItem>,
    private val onItemClick: (WisataItem) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var isGridView = false

    fun updateData(newList: List<WisataItem>) {
        listWisata = newList
        notifyDataSetChanged()
    }

    companion object {
        private const val VIEW_TYPE_LIST = 0
        private const val VIEW_TYPE_GRID = 1
    }

    fun setGridView(isGrid: Boolean) {
        this.isGridView = isGrid
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (isGridView) VIEW_TYPE_GRID else VIEW_TYPE_LIST
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_GRID) {
            val binding = ItemWisataGridBinding.inflate(inflater, parent, false)
            GridViewHolder(binding)
        } else {
            val binding = ItemWisataBinding.inflate(inflater, parent, false)
            ListViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val wisata = listWisata[position]
        if (holder is ListViewHolder) {
            holder.bind(wisata)
        } else if (holder is GridViewHolder) {
            holder.bind(wisata)
        }
    }

    override fun getItemCount(): Int = listWisata.size

    inner class ListViewHolder(private val binding: ItemWisataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: WisataItem) {
            with(binding) {
                val context = root.context
                val coverImageName = if (item.gallery.isNotEmpty()) item.gallery[0] else "wisata${item.id}"
                val resId = context.resources.getIdentifier(coverImageName, "drawable", context.packageName)
                imgWisata.setImageResource(if (resId != 0) resId else R.drawable.wisata1)

                tvName.text = item.name
                tvLocation.text = item.location
                tvRating.text = "⭐ ${item.rating}"
                
                root.setOnClickListener { onItemClick(item) }
            }
        }
    }

    inner class GridViewHolder(private val binding: ItemWisataGridBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: WisataItem) {
            with(binding) {
                val context = root.context
                val coverImageName = if (item.gallery.isNotEmpty()) item.gallery[0] else "wisata${item.id}"
                val resId = context.resources.getIdentifier(coverImageName, "drawable", context.packageName)
                imgWisata.setImageResource(if (resId != 0) resId else R.drawable.wisata1)

                tvName.text = item.name
                tvLocation.text = item.location
                tvRating.text = "⭐ ${item.rating}"
                
                root.setOnClickListener { onItemClick(item) }
            }
        }
    }
}
