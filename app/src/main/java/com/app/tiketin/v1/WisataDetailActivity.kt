package com.app.tiketin.v1

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.tiketin.v1.data.TiketinRepository
import com.app.tiketin.v1.databinding.ActivityWisataDetailBinding
import com.app.tiketin.v1.model.PaketWisata
import com.app.tiketin.v1.model.WisataItem
import com.google.android.material.chip.Chip
import java.text.NumberFormat
import java.util.Locale

class WisataDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ID = "extra_id"
    }

    private lateinit var binding: ActivityWisataDetailBinding
    private var isDescriptionExpanded = false

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

            // Packages
            layoutPackages.removeAllViews()
            item.tourPackages.forEach { paket ->
                val packageView = createPackageView(paket)
                layoutPackages.addView(packageView)
            }

            btnBookNow.setOnClickListener {
                Toast.makeText(this@WisataDetailActivity, getString(R.string.payment_coming_soon), Toast.LENGTH_SHORT).show()
            }
        }
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