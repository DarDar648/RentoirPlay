package com.app.tiketin.v1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.tiketin.v1.adapter.HistoryAdapter
import com.app.tiketin.v1.data.HistoryRepository
import com.app.tiketin.v1.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: UserSessionManager
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = UserSessionManager(requireContext())
        
        setupRecyclerView()
        loadHistory()
    }

    private fun setupRecyclerView() {
        historyAdapter = HistoryAdapter(emptyList()) { item ->
            showDeleteConfirmationDialog(item.id)
        }
        binding.rvHistory.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = historyAdapter
        }
    }

    private fun loadHistory() {
        val username = sessionManager.getUsername()
        if (username != null) {
            val historyList = HistoryRepository.getHistory(requireContext(), username)
            historyAdapter.updateData(historyList)
            if (historyList.isEmpty()) {
                Toast.makeText(requireContext(), "Belum ada riwayat pemesanan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDeleteConfirmationDialog(itemId: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Hapus Riwayat")
            .setMessage("Apakah Anda yakin ingin menghapus riwayat pembelian ini?")
            .setPositiveButton("Hapus") { _, _ ->
                HistoryRepository.deleteHistoryItem(requireContext(), itemId)
                Toast.makeText(requireContext(), "Riwayat dihapus", Toast.LENGTH_SHORT).show()
                loadHistory()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}