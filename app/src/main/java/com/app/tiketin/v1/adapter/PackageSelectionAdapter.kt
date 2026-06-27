package com.app.tiketin.v1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.tiketin.v1.R
import com.app.tiketin.v1.databinding.ItemPackageSelectableBinding
import com.app.tiketin.v1.model.PaketWisata
import java.text.NumberFormat
import java.util.Locale

class PackageSelectionAdapter(
    private val packages: List<PaketWisata>,
    private val onPackageSelected: (PaketWisata) -> Unit
) : RecyclerView.Adapter<PackageSelectionAdapter.ViewHolder>() {

    private var selectedPosition = -1

    inner class ViewHolder(val binding: ItemPackageSelectableBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPackageSelectableBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val paket = packages[position]
        with(holder.binding) {
            tvPackageName.text = paket.name
            tvPackageDuration.text = "⏳ ${paket.duration}"
            tvPackagePrice.text = formatCurrency(paket.price)
            
            // Format facilities: • Fac 1 • Fac 2
            tvPackageFacilities.text = paket.facilities.joinToString(" • ", prefix = "• ")

            val context = root.context
            if (position == selectedPosition) {
                cardPackage.strokeColor = context.getColor(R.color.brand_primary)
                cardPackage.strokeWidth = 6
                cardPackage.setCardBackgroundColor(context.getColor(R.color.surface_soft))
                ivSelected.visibility = View.VISIBLE
            } else {
                cardPackage.strokeColor = context.getColor(R.color.surface_variant)
                cardPackage.strokeWidth = 2
                cardPackage.setCardBackgroundColor(context.getColor(R.color.surface))
                ivSelected.visibility = View.GONE
            }

            root.setOnClickListener {
                val previousSelected = selectedPosition
                selectedPosition = holder.bindingAdapterPosition
                if (previousSelected != -1) {
                    notifyItemChanged(previousSelected)
                }
                notifyItemChanged(selectedPosition)
                onPackageSelected(paket)
            }
        }
    }

    override fun getItemCount(): Int = packages.size

    private fun formatCurrency(amount: Int): String {
        val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        return format.format(amount).replace(",00", "")
    }
}
