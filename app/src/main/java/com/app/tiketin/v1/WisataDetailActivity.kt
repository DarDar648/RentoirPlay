package com.app.tiketin.v1

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
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
import com.app.tiketin.v1.data.DatabaseHelper
import com.app.tiketin.v1.data.HistoryRepository
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
    private lateinit var sessionManager: UserSessionManager
    private lateinit var dbHelper: DatabaseHelper
    private var isDescriptionExpanded = false
    private var selectedPaket: PaketWisata? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWisataDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = UserSessionManager(this)
        dbHelper = DatabaseHelper(this)

        setupToolbar()
        checkNotificationPermission()

        val id = intent.getIntExtra(EXTRA_ID, -1)
        val item = dbHelper.getWisataById(id)

        if (item != null) {
            displayData(item)
        } else {
            Toast.makeText(this, "Data tidak ditemukan di database", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 101)
            }
        }
    }

    private fun displayData(item: WisataItem) {
        with(binding) {
            // Mengambil gambar berdasarkan ID wisata
            val imageName = "wisata${item.id}"
            val resId = resources.getIdentifier(imageName, "drawable", packageName)
            imgWisata.setImageResource(if (resId != 0) resId else R.drawable.wisata1)

            tvName.text = item.name
            tvLocation.text = item.location
            tvRating.text = item.rating.toString()
            tvReviewCount.text = "(${item.reviewCount} ulasan)"
            tvPrice.text = formatCurrency(item.price)
            tvDesc.text = item.description

            btnReadMore.setOnClickListener {
                if (isDescriptionExpanded) {
                    tvDesc.maxLines = 4
                    btnReadMore.text = "Selengkapnya"
                } else {
                    tvDesc.maxLines = Int.MAX_VALUE
                    btnReadMore.text = "Lebih Sedikit"
                }
                isDescriptionExpanded = !isDescriptionExpanded
            }

            chipGroupFacilities.removeAllViews()
            item.facilities.forEach { facility ->
                val chip = Chip(this@WisataDetailActivity).apply {
                    text = facility
                    isClickable = false
                }
                chipGroupFacilities.addView(chip)
            }

            tvOperational.text = "Jam Operasional: ${item.operationalHours}"
            tvPhone.text = "Telepon: ${item.phoneNumber}"
            tvWebsite.text = "Website: ${item.website}"

            layoutGallery.removeAllViews()
            item.gallery.forEach { imgRes ->
                val imageView = ImageView(this@WisataDetailActivity).apply {
                    val size = 200
                    layoutParams = LinearLayout.LayoutParams(size, size).apply {
                        setMargins(0, 0, 16, 0)
                    }
                    setImageResource(imgRes)
                    scaleType = ImageView.ScaleType.CENTER_CROP
                }
                layoutGallery.addView(imageView)
            }

            layoutPackages.removeAllViews()
            item.tourPackages.forEach { paket ->
                layoutPackages.addView(createPackageView(paket))
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

        selectedPaket = null

        val adapter = PackageSelectionAdapter(wisata.tourPackages) { paket ->
            selectedPaket = paket
        }

        bottomSheetBinding.rvPackages.apply {
            layoutManager = LinearLayoutManager(this@WisataDetailActivity)
            this.adapter = adapter
        }

        bottomSheetBinding.btnConfirm.setOnClickListener {
            if (selectedPaket != null) {
                bottomSheetBinding.layoutSelection.visibility = View.GONE
                bottomSheetBinding.layoutPayment.visibility = View.VISIBLE
                bottomSheetBinding.tvPaymentAmount.text = "Total: ${formatCurrency(selectedPaket!!.price)}"
            } else {
                Toast.makeText(this, "Pilih paket terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
        }

        bottomSheetBinding.btnFinalConfirm.setOnClickListener {
            val date = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date())
            val username = sessionManager.getUsername()

            if (username == null) {
                Toast.makeText(this, "Session error!", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
                return@setOnClickListener
            }

            val historyItem = HistoryItem(
                id = System.currentTimeMillis().toString(),
                wisataName = wisata.name,
                packageName = selectedPaket?.name ?: "",
                price = selectedPaket?.price ?: 0,
                date = date,
                wisataId = wisata.id
            )

            HistoryRepository.addHistory(this@WisataDetailActivity, username, historyItem)

            // Send Broadcast for notification
            val intent = Intent("com.app.tiketin.v1.ACTION_TICKET_BOOKED").apply {
                putExtra("WISATA_NAME", wisata.name)
                putExtra("PACKAGE_NAME", selectedPaket?.name ?: "")
                setPackage(packageName)
            }
            sendBroadcast(intent)

            Toast.makeText(this, "Pemesanan ${selectedPaket?.name} Berhasil!", Toast.LENGTH_LONG).show()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun createPackageView(paket: PaketWisata): View {
        val container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(16, 16, 16, 16)
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                setMargins(0, 0, 0, 8)
            }
            setBackgroundResource(android.R.drawable.dialog_holo_light_frame)
        }

        val tvPackageName = TextView(this).apply {
            text = paket.name
            textSize = 16f
            setTypeface(null, android.graphics.Typeface.BOLD)
        }
        container.addView(tvPackageName)

        val tvPackagePrice = TextView(this).apply {
            text = formatCurrency(paket.price)
            textSize = 14f
            setTextColor(getColor(android.R.color.holo_blue_dark))
        }
        container.addView(tvPackagePrice)

        return container
    }

    private fun formatCurrency(amount: Int): String {
        val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        return format.format(amount).replace(",00", "")
    }
}
