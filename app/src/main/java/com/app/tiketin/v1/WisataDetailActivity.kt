package com.app.tiketin.v1

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.tiketin.v1.adapter.PackageSelectionAdapter
import com.app.tiketin.v1.data.HistoryRepository
import com.app.tiketin.v1.data.TiketinRepository
import com.app.tiketin.v1.databinding.ActivityWisataDetailBinding
import com.app.tiketin.v1.databinding.BottomSheetPackageBinding
import com.app.tiketin.v1.model.HistoryItem
import com.app.tiketin.v1.model.PaketWisata
import com.app.tiketin.v1.model.WisataItem
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WisataDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ID = "extra_id"
    }

    private lateinit var binding: ActivityWisataDetailBinding
    private var isDescriptionExpanded = false
    private var selectedPaket: PaketWisata? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWisataDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()

        val id = intent.getIntExtra(EXTRA_ID, -1)
        val item = TiketinRepository.getWisataById(id)

        if (item != null) {
            displayData(item)
        } else {
            Toast.makeText(this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun displayData(item: WisataItem) {
        with(binding) {
            imgWisata.setImageResource(item.imageResId)
            tvName.text = item.name
            tvLocation.text = item.location
            tvRating.text = item.rating.toString()
            tvReviewCount.text = getString(R.string.review_count_format, item.reviewCount)
            tvPrice.text = formatCurrency(item.price)
            tvDesc.text = item.description

            // Expandable Description
            btnReadMore.setOnClickListener {
                if (isDescriptionExpanded) {
                    tvDesc.maxLines = 4
                    btnReadMore.text = getString(R.string.read_more)
                } else {
                    tvDesc.maxLines = Int.MAX_VALUE
                    btnReadMore.text = getString(R.string.read_less)
                }
                isDescriptionExpanded = !isDescriptionExpanded
            }

            // Facilities
            chipGroupFacilities.removeAllViews()
            item.facilities.forEach { facility ->
                val chip = Chip(this@WisataDetailActivity).apply {
                    text = facility
                    isClickable = false
                    setChipBackgroundColorResource(R.color.surface_soft)
                }
                chipGroupFacilities.addView(chip)
            }

            // Important Info
            tvOperational.text = getString(R.string.operational_hours_format, item.operationalHours)
            tvPhone.text = getString(R.string.phone_format, item.phoneNumber)
            tvWebsite.text = getString(R.string.website_format, item.website)

            // Gallery
            layoutGallery.removeAllViews()
            item.gallery.forEach { imgRes ->
                val imageView = ImageView(this@WisataDetailActivity).apply {
                    val size = resources.getDimensionPixelSize(R.dimen.space_xl) * 4
                    layoutParams = LinearLayout.LayoutParams(size, size).apply {
                        setMargins(0, 0, resources.getDimensionPixelSize(R.dimen.space_sm), 0)
                    }
                    setImageResource(imgRes)
                    scaleType = ImageView.ScaleType.CENTER_CROP
                    clipToOutline = true
                    setBackgroundResource(R.drawable.ic_launcher_background)
                    setOnClickListener {
                        Toast.makeText(context, getString(R.string.opening_photo), Toast.LENGTH_SHORT).show()
                    }
                }
                layoutGallery.addView(imageView)
            }

            // Packages (Listing in detail page)
            layoutPackages.removeAllViews()
            item.tourPackages.forEach { paket ->
                val packageView = createPackageView(paket)
                layoutPackages.addView(packageView)
            }

            btnBookNow.setOnClickListener {
                showPackageSelectionBottomSheet(item)
            }
        }
    }

    private fun showPackageSelectionBottomSheet(wisata: WisataItem) {
        val bottomSheetBinding = BottomSheetPackageBinding.inflate(layoutInflater)
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(bottomSheetBinding.root)

        selectedPaket = null // Reset selection

        val adapter = PackageSelectionAdapter(wisata.tourPackages) { paket ->
            selectedPaket = paket
        }

        bottomSheetBinding.rvPackages.apply {
            layoutManager = LinearLayoutManager(this@WisataDetailActivity)
            this.adapter = adapter
        }

        // Step 1 -> Step 2 (Selection to Payment)
        bottomSheetBinding.btnConfirm.setOnClickListener {
            if (selectedPaket != null) {
                bottomSheetBinding.layoutSelection.visibility = View.GONE
                bottomSheetBinding.layoutPayment.visibility = View.VISIBLE
                bottomSheetBinding.tvPaymentAmount.text = "Total: ${formatCurrency(selectedPaket!!.price)}"
                bottomSheetBinding.ivQrCode.setImageResource(R.drawable.ic_launcher_background)
            } else {
                Toast.makeText(this, getString(R.string.msg_select_package_first), Toast.LENGTH_SHORT).show()
            }
        }

        // Step 2 Finalization
        bottomSheetBinding.btnFinalConfirm.setOnClickListener {
            val date = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date())
            
            // Save to History Repository
            val historyItem = HistoryItem(
                id = System.currentTimeMillis().toString(),
                wisataName = wisata.name,
                packageName = selectedPaket?.name ?: "",
                price = selectedPaket?.price ?: 0,
                date = date,
                imageResId = wisata.imageResId
            )
            HistoryRepository.addHistory(historyItem)

            Toast.makeText(
                this,
                getString(R.string.msg_booking_success, selectedPaket?.name, formatCurrency(selectedPaket!!.price)),
                Toast.LENGTH_LONG
            ).show()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun createPackageView(paket: PaketWisata): View {
        val container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(
                resources.getDimensionPixelSize(R.dimen.space_md),
                resources.getDimensionPixelSize(R.dimen.space_md),
                resources.getDimensionPixelSize(R.dimen.space_md),
                resources.getDimensionPixelSize(R.dimen.space_md)
            )
            val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 0, 0, resources.getDimensionPixelSize(R.dimen.space_sm))
            layoutParams = params
            setBackgroundResource(R.drawable.ic_launcher_background)
            backgroundTintList = getColorStateList(R.color.surface_soft)
            elevation = 2f

            setOnClickListener {
                Toast.makeText(this@WisataDetailActivity, getString(R.string.selecting_package, paket.name), Toast.LENGTH_SHORT).show()
            }
        }

        val tvPackageName = TextView(this).apply {
            text = paket.name
            textSize = 16f
            setTextColor(getColor(R.color.text_primary))
            setTypeface(null, android.graphics.Typeface.BOLD)
        }
        container.addView(tvPackageName)

        val tvPackagePrice = TextView(this).apply {
            text = formatCurrency(paket.price)
            textSize = 14f
            setTextColor(getColor(R.color.brand_primary))
            setTypeface(null, android.graphics.Typeface.BOLD)
        }
        container.addView(tvPackagePrice)

        val tvDuration = TextView(this).apply {
            text = getString(R.string.duration_format, paket.duration)
            textSize = 12f
            setTextColor(getColor(R.color.text_secondary))
        }
        container.addView(tvDuration)

        return container
    }

    private fun formatCurrency(amount: Int): String {
        val format = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("id-ID"))
        return format.format(amount).replace(",00", "")
    }
}