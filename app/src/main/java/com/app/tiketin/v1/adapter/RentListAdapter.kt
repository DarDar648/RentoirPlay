package com.app.tiketin.v1.ui
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.app.tiketin.v1.databinding.ItemRentRowBinding
import com.app.tiketin.v1.model.RentItem
import java.text.NumberFormat
import java.util.Locale
class RentListAdapter(
    context: Context,
    private val items: List<RentItem>
) : ArrayAdapter<RentItem>(context, 0, items) {
    private val rupiah =
        NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    override fun getView(position: Int, convertView: View?, parent:
    ViewGroup): View {
        val binding: ItemRentRowBinding
        val view: View
        if (convertView == null) {
            binding =
                ItemRentRowBinding.inflate(LayoutInflater.from(context), parent,
                    false)
            view = binding.root
            view.tag = binding
        } else {
            view = convertView
            binding = view.tag as ItemRentRowBinding
        }
        val item = items[position]
        binding.imgRent.setImageResource(item.imageResId)
        binding.tvName.text = item.name
        binding.tvRestaurant.text = item.restaurant
        binding.tvPrice.text = rupiah.format(item.price)
        binding.tvRating.text = "⭐ ${item.rating}"
        return view
    }
}