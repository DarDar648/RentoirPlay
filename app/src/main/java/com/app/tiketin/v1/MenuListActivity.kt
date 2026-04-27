package com.app.tiketin.v1
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.tiketin.v1.data.TiketinRepository
import com.app.tiketin.v1.databinding.ActivityMenuListBinding
import com.app.tiketin.v1.ui.RentListAdapter
class MenuListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val rents = TiketinRepository.getRents()
        val adapter = RentListAdapter(this, rents)
        binding.listRents.adapter = adapter
        // Tahap 4: nanti klik item -> pindah ke detail pakai Intent + extras
        binding.listRents.setOnItemClickListener { _, _, position,
                                                   _ ->
            val item = rents[position]
            val intent = Intent(this,
                RentDetailActivity::class.java)
            intent.putExtra(RentDetailActivity.EXTRA_RENT_ID, item.id)
            startActivity(intent)
        }
    }
}
